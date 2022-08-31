import tensorflow as tf
import os
import datetime
from model import Encoder, Decoder
from jieba import lcut, add_word
import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'

class Seq2Seq_model():
    def __init__(self):
        self.data_path = './ids'  # path to dataset
        self.epoch = 3000  # the number of epochs 5000
        self.batch_size = 64  # 一次前向/后向传播中提供的训练数据样本数
        self.embedding_dim = 256  # 词嵌入维度
        self.hidden_dim = 512  # 隐层神经元个数
        self.shuffle_buffer_size = 4  # 清洗数据集时将缓冲的实例数
        self.device = 0  # 使用的设备ID，-1即不使用GPU
        self.checkpoint_path = './model'  # 模型参数保存的路径
        self.MAX_LENGTH = 50  # 句子的最大词长
        self.CONST = {'_BOS': 0, '_EOS': 1, '_PAD': 2, '_UNK': 3}

    def prepare_data(self):
        # 模型参数保存的路径如果不存在则新建
        if not os.path.exists(self.checkpoint_path):
            os.makedirs(self.checkpoint_path)

        # 获得当前主机上GPU运算设备的列表
        gpus = tf.config.experimental.list_physical_devices('GPU')
        if 0 <= self.device and 0 < len(gpus):
            # 限制TensorFlow仅使用指定的GPU
            tf.config.experimental.set_visible_devices(gpus[self.device], 'GPU')
            logical_gpus = tf.config.experimental.list_logical_devices('GPU')
            print(f'[{datetime.datetime.now()}] {len(gpus)} physical GPUs')
            print(f'[{datetime.datetime.now()}] {len(logical_gpus)} logical GPUs')

        # 加载词典
        print(f'[{datetime.datetime.now()}] 加载词典...')
        table = tf.lookup.StaticHashTable(  # 初始化后即不可变的通用哈希表。词表
            initializer=tf.lookup.TextFileInitializer(
                os.path.join(self.data_path, 'all_dict.txt'),  # 文本文件的文件名
                tf.string,  # 该key数据类型
                tf.lookup.TextFileIndex.WHOLE_LINE,  # 表示从中获取表 “键” 值的行信息的索引
                tf.int64,  # value数据类型
                tf.lookup.TextFileIndex.LINE_NUMBER  # 表示从中获取表 “值” 值的行信息的索引
            ),  # 要使用的表初始化程序。有关支持的键和值类型，请参见HashTable内核。
            default_value=self.CONST['_UNK'] - len(self.CONST)  # 表中缺少键时使用的值。
        )

        # 加载数据
        print(f'[{datetime.datetime.now()}] 加载预处理后的数据...')

        def get_dataset(src_path: str, table: tf.lookup.StaticHashTable) -> tf.data.Dataset:

            def to_ids(text):
                tokenized = tf.strings.split(tf.reshape(text, [1]), sep=' ')  # 将文本一个字一个字隔开
                ids = table.lookup(tokenized.values) + len(self.CONST)  # 查找tokenized.values表中的，输出对应的值
                return ids

            def add_start_end_tokens(tokens):
                ids = tf.concat(
                    [[self.CONST['_BOS']], tf.cast(tokens, tf.int32), [self.CONST['_EOS']]]
                    , axis=0
                )
                return ids

            dataset = tf.data.TextLineDataset(src_path)
            dataset = dataset.map(to_ids)
            dataset = dataset.map(add_start_end_tokens)

            return dataset

        # -----------------------------------------------------------------------------

        src_train = get_dataset(os.path.join(self.data_path, 'source.txt'), table)
        tgt_train = get_dataset(os.path.join(self.data_path, 'target.txt'), table)
        train_dataset = tf.data.Dataset.zip((src_train, tgt_train))

        def filter_instance_by_max_length(src: tf.Tensor, tgt: tf.Tensor) -> tf.Tensor:
            return tf.logical_and(tf.size(src) <= self.MAX_LENGTH, tf.size(tgt) <= self.MAX_LENGTH)  # 逻辑与

        train_dataset = train_dataset.filter(filter_instance_by_max_length)
        train_dataset = train_dataset.cache()  # 缓存此数据集中的元素
        train_dataset = train_dataset.shuffle(self.shuffle_buffer_size)  # 该数据集中的元素数量
        train_dataset = train_dataset.padded_batch(
            self.batch_size,
            padded_shapes=([self.MAX_LENGTH + 2], [self.MAX_LENGTH + 2]),
            padding_values=(self.CONST['_PAD'], self.CONST['_PAD']),  # 各个组件的填充值
            drop_remainder=True,  # 在最后一批少于元素的情况下删除它
        )
        train_dataset = train_dataset.prefetch(tf.data.experimental.AUTOTUNE)  # 创建一个Dataset从该数据集中预取元素的

        return table, train_dataset

    def loss_function(self, loss_object, real: tf.Tensor, pred: tf.Tensor) -> tf.Tensor:
        loss_ = loss_object(real, pred)
        mask = tf.math.logical_not(tf.math.equal(real, self.CONST['_PAD']))
        mask = tf.cast(mask, dtype=loss_.dtype)  # 将张量转换为新类型
        return tf.reduce_mean(loss_ * mask)

    def model(self):
        table, train_dataset = self.prepare_data()
        # create a model
        print(f'[{datetime.datetime.now()}] Creating a seq2seq model...')
        encoder = Encoder(
            table.size().numpy() + len(self.CONST),
            self.embedding_dim,
            self.hidden_dim
        )
        decoder = Decoder(
            table.size().numpy() + len(self.CONST),
            self.embedding_dim,
            self.hidden_dim
        )

        # set up the optimizer
        print(f'[{datetime.datetime.now()}] 准备优化器...')
        optimizer = tf.keras.optimizers.Adam()  # 实现 Adam 算法的优化器

        # set up the objective function
        print(f'[{datetime.datetime.now()}] Setting up the objective function...')
        loss_object = tf.keras.losses.SparseCategoricalCrossentropy(  # 计算标签和预测之间的交叉熵损失
            from_logits=True, reduction='none'
        )

        # set up the saver
        checkpoint = tf.train.Checkpoint(  # 管理将可跟踪值保存 / 恢复到磁盘
            optimizer=optimizer, encoder=encoder, decoder=decoder
        )
        return encoder, decoder, checkpoint, loss_object, optimizer

    def train(self):
        encoder, decoder, checkpoint, loss_object, optimizer = self.model()
        table, train_dataset = self.prepare_data()

        @tf.function
        def train_step(src: tf.Tensor, tgt: tf.Tensor):
            _, tgt_length = tgt.shape
            loss = 0

            with tf.GradientTape() as tape:  # 记录自动微分操作
                enc_output, enc_hidden = encoder(src)

                dec_hidden = enc_hidden

                for t in range(tgt_length - 1):
                    # using teacher forcing
                    dec_input = tf.expand_dims(tgt[:, t], 1)

                    # passing enc_output to the decoder
                    predictions, dec_hidden, _ = decoder(dec_input, dec_hidden, enc_output)

                    loss += self.loss_function(loss_object, tgt[:, t + 1], predictions)

            batch_loss = loss / tgt_length

            variables = encoder.trainable_variables + decoder.trainable_variables

            gradients = tape.gradient(loss, variables)  # 使用记录在此磁带上下文中的操作计算梯度

            optimizer.apply_gradients(zip(gradients, variables))

            return batch_loss

        print(f'[{datetime.datetime.now()}] 开始训练模型.')
        for epoch in range(self.epoch):
            total_loss = 0

            for batch, (src, tgt) in enumerate(train_dataset):
                batch_loss = train_step(src, tgt)
                total_loss += batch_loss

                if batch % 100 == 0:
                    print(f'[{datetime.datetime.now()}] Epoch {epoch} Batch {batch} Loss {batch_loss.numpy():.4f}')

            if epoch % 100 == 0:
                # saving (checkpoint) the model
                checkpoint_prefix = os.path.join(self.checkpoint_path, "ckpt")
                checkpoint.save(file_prefix=checkpoint_prefix)

            print(f'[{datetime.datetime.now()}] Epoch {epoch} Loss {total_loss:.4f}')

    def predict(self, sentence='你好'):
        encoder, decoder, checkpoint, loss_object, optimizer = self.model()
        # 模型测试
        checkpoint.restore(tf.train.latest_checkpoint(self.checkpoint_path))
        sentence = '_BOS' + sentence + '_EOS'
        with open(os.path.join(self.data_path, 'all_dict.txt'), 'r', encoding='utf-8') as f:
            all_dict = f.read().split()

        word2id = {j: i + len(self.CONST) for i, j in enumerate(all_dict)}
        word2id.update(self.CONST)
        id2word = dict(zip(word2id.values(), word2id.keys()))
        for i in ['_EOS', '_BOS']:
            add_word(i)
        inputs = [word2id.get(i, self.CONST['_UNK']) for i in lcut(sentence)]
        inputs = tf.keras.preprocessing.sequence.pad_sequences(
            [inputs], maxlen=self.MAX_LENGTH, padding='post', value=self.CONST['_PAD']
        )
        inputs = tf.convert_to_tensor(inputs)
        print(inputs)
        result = ''
        # hidden = [tf.zeros((1, self.hidden_dim))]
        enc_out, enc_hidden = encoder(inputs)

        dec_hidden = enc_hidden
        dec_input = tf.expand_dims([word2id['_BOS']], 0)

        for t in range(self.MAX_LENGTH):
            predictions, dec_hidden, attention_weights = decoder(dec_input, dec_hidden, enc_out)

            predicted_id = tf.argmax(predictions[0]).numpy()

            if id2word.get(predicted_id, '_UNK') == '_EOS':
                break
            result += id2word.get(predicted_id, '_UNK')

            dec_input = tf.expand_dims([predicted_id], 0)

        return result


if __name__ == '__main__':
    MODEL = Seq2Seq_model()
    MODEL.train()
