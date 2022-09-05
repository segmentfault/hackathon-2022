import React from 'react';
import { MenuItem, useToast } from '@chakra-ui/react';
import { useCopyToClipboard } from 'react-use';

const toastSettings = {
  status: 'success',
  duration: 2000,
  position: 'top',
  isClosable: true,
};

const menuCopyArr=[
  {type:'url',name:'复制 URL'},
  {type:'title',name:'复制 标题'},
  {type:'urlAndTitle',name:'复制 URL 和标题'},
  {type:'md',name:'复制 Markdown 格式'},
]
// 右键复制相关内容
export default function CardRightMenuCopy({ itemData }) {
  const toast = useToast();
  const [state, copyToClipboard] = useCopyToClipboard();

  const copyByType = (copyContent, titleToastContent) => {
    copyToClipboard(copyContent);
    toast({
      title: titleToastContent,
      ...toastSettings,
    });
  };

  const cardCopy = (type, item) => {
    const { url, title } = item;
    switch (type) {
      case 'url':
        copyByType(url, `链接 ${url} 已经复制到剪切板`);
        break;
      case 'title':
        copyByType(title, `标题 ${title} 已经复制到剪切板`);
        break;
      case 'urlAndTitle':
        copyByType(`${title} ${url}`, `链接和标题已经复制到剪切板`);
        break;
      case 'md':
        copyByType(`[${title}](${url})`, `Markdown 格式已经复制到剪切板`);
        break;
      default: copyByType(url, `链接 ${url} 已经复制到剪切板`);
    }
  };

  return (
    <>
      { menuCopyArr.map((item,idx)=>{
        const { type,name }=item;
        return(
          <MenuItem onClick={() => cardCopy(type, itemData)} key={type}>{ name }</MenuItem>
        )
      }) }
    </>
  );
}
