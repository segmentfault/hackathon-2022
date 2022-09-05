import React from 'react';

import { Box, Button } from '@chakra-ui/react';
// 新增书签的按钮
export default function AddLinkBtn({ addLink }) {

  return (
    <Box
      borderRadius={'50%'}
      bg={'#FFD8A9'}
      boxShadow={'0px 1px 25px -5px #daa8667a, 0 10px 10px -5px #bb905d'}
      display='flex'
      position={'fixed'}
      bottom={'20px'}
      right={'20px'}
      zIndex={10}
      w={'110px'}
      h={'110px'}
      onClick={() => addLink(true)}
    >
      <Button bg={'#F1A661'}
              _hover={{ bg: 'orange' }}
              transform={'translate(-50%,-50%)'}
              left={'50%'}
              top={'50%'}
      >添加 </Button>
    </Box>
  );
}
