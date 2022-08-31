from flask import Flask, render_template, request, jsonify
from execute import Seq2Seq_model

Model = Seq2Seq_model()

app = Flask(__name__, static_url_path='/static')

@app.route('/message', methods=['POST'])
def reply():
    req_msg = request.form['msg']  # 获取信息
    if req_msg == '':
        res_msg = '请与我聊天吧!'
    else:
        res_msg = Model.predict(req_msg)  # 模型输出
    return jsonify({'text': res_msg})


@app.route('/')
def index():
    return render_template('index.html')


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=8008)
