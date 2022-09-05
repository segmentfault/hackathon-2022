import React from 'react';
import {
  Button, Modal, ModalOverlay, ModalContent, ModalHeader, ModalFooter,
  ModalBody, ModalCloseButton, useToast, Flex,
  FormControl, FormLabel, Input,  Switch,
} from '@chakra-ui/react';
import axios from 'axios';
import dayjs from 'dayjs';
import { MdFavorite } from 'react-icons/md';

const urlReg = /^((https?|ftp|file):\/\/)?([\da-z\.-]+)\.([a-z\.]{2,6})([\/\w \.-]*)*\/?$/;

// 添加链接弹窗
export default function AddLinkModal({ isOpen, onClose }) {

  const toast = useToast();
  const initialRef = React.useRef(null);
  const finalRef = React.useRef(null);

  const [url, setUrlValue] = React.useState('');
  const [loadingState, setLoadingState] = React.useState(false);
  const [favorite, setFavorite] = React.useState(false);

  const urlInputChange = (event) => {
    let originValue = event.target.value;
    let formatValue = (originValue + '').trim();
    setUrlValue(formatValue);
  };
  // 解析失败时候的 toast
  const failToast = {
    title: `解析失败！`,
    status: 'error',
    duration: 2000,
    position: 'top',
    isClosable: true,
  };

  const parseBtn = () => {
    if (urlReg.test(url)) {
      parseUrl();
    } else {
      toast({
        title: `请检查 URL 格式是否正确！`,
        status: 'error',
        duration: 2000,
        position: 'top',
        isClosable: true,
      });
    }
  };

  const parseUrl = () => {
    setLoadingState(true);

    const options = {
      method: 'GET',
      url: 'https://article-extractor2.p.rapidapi.com/article/parse',
      params: { url },
      headers: {
        'X-RapidAPI-Key': 'be767c3568mshf30d02261de143bp13757djsn678913eddeaf', // 每个月1000次请求 限制
        'X-RapidAPI-Host': 'article-extractor2.p.rapidapi.com',
      },
    };

    axios.request(options).then(function(res) {
      const resData = res.data;
      if (res.status === 200) {
        if (resData.error === 0) {
          setLoadingState(false);
          toast({
            title: `解析成功`,
            status: 'success',
            duration: 2000,
            position: 'top',
            isClosable: true,
          });
          const { description, title, image } = resData.data;
          let finalUrlInfo = {
            url,
            title,
            image,
            description,
            isFavorite: favorite,
            updatedAt: dayjs().format('YYYY-MM-DD'),
          };
          onClose();
          let getStoreData = localStorage.getItem('bookmarkData');
          let storeData = JSON.parse(getStoreData);
          storeData.list.push(finalUrlInfo);
          localStorage.setItem('bookmarkData', JSON.stringify(storeData));
        } else {
          toast(failToast);
          setLoadingState(false);
          onClose();
        }
      }
    }).catch(function(error) {
      toast(failToast);
      setLoadingState(false);
      onClose();
      console.error(error);
    });
  };

  return (
    <Modal
      initialFocusRef={initialRef}
      finalFocusRef={finalRef}
      isOpen={isOpen}
      onClose={onClose}
    >
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>添加链接</ModalHeader>
        <ModalCloseButton />
        <ModalBody pb={6}>
          <FormControl>
            <FormLabel>链接 URL</FormLabel>
            <Input ref={initialRef}
                   onChange={urlInputChange}
                   _focus={{
                     boxShadow: 'none',
                   }}
                   placeholder='请输入格式正确且完整的 URL' />
            <Flex mt={4} align='center'>
              <MdFavorite w='26px' h='26px' color={'gold'} />
              <FormLabel htmlFor='isChecked' mb={0} pl={2}>是否设置星标:</FormLabel>
              <Switch id='isChecked'
                      isChecked={favorite}
                      isDisabled={loadingState} size={'md'}
                      onChange={() => setFavorite(!favorite)} />
            </Flex>
          </FormControl>

        </ModalBody>
        <ModalFooter>
          <Button bgColor={'#FFD8A9'} mr={3}
                  _hover={{ bgColor: '#F1A661' }}
                  onClick={parseBtn}
                  isLoading={loadingState}
          >确定</Button>
          <Button onClick={onClose}>取消</Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}
