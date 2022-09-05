import React from 'react';
import {
  Avatar,
  Box,
  Flex,
  HStack,
  IconButton,
  Menu,
  MenuButton, Button, MenuItem, MenuList,
  Text,
  useColorModeValue,
  VStack, Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalFooter,
  ModalBody,
  ModalCloseButton,
  useDisclosure,
} from '@chakra-ui/react';
import { FiChevronDown, FiMenu } from 'react-icons/fi';

const MobileNav = ({ isHShow, data, updateBookmak }) => {
  const { isOpen, onOpen, onClose } = useDisclosure();

  const clearData = () => {
    onClose();
    if (data.length > 0) {
      let bookmarkData = {
        list: [],
      };
      localStorage.setItem('bookmarkData', JSON.stringify(bookmarkData));
      updateBookmak([]);
    }
  };

  return (
    <Flex
      position='fixed' zIndex={'100'}
      top={'20px'}
      right={'15px'}
      px={{ base: 4, md: 4 }}
      w={{
        base: 'calc(100vw - 6%)',
        md: 'calc(100vw - 8%)',
        lg: 'calc(100vw - 6%)',
        xl: 'calc(100vw - 350px)',
        '2xl': 'calc(100vw - 365px)',
      }}
      height='20'
      alignItems='center'
      bg={'#FFD8A9'}
      borderRadius={'12px'}
      justifyContent={{ base: 'space-between', md: 'space-between', lg: 'flex-end' }}
    >

      <IconButton
        display={{ base: 'flex', lg: 'none' }}
        onClick={isHShow}
        variant='outline'
        aria-label='打开菜单'
        icon={<FiMenu />}
      />

      <Text
        display={{ base: 'flex', sm: 'flex', lg: 'none' }}
        fontSize='2xl'
        fontFamily='monospace'
        fontWeight='bold'>
        不吃灰
      </Text>

      <HStack spacing={{ base: '0', md: '6' }}>

        <Flex alignItems={'center'}>
          <Menu>
            <MenuButton
              py={2}
              transition='all 0.3s'
              _focus={{ boxShadow: 'none' }}>
              <HStack>
                <Avatar
                  size={'sm'}
                  src={'https://bit.ly/dan-abramov'}
                />
                <VStack
                  display={{ base: 'none', md: 'flex' }}
                  alignItems='flex-start'
                  spacing='1px'
                  ml='2'>
                  <Text fontSize='sm'>小灰灰</Text>
                </VStack>
                <Box display={{ base: 'none', md: 'flex' }}>
                  <FiChevronDown />
                </Box>
              </HStack>
            </MenuButton>
            <MenuList
              bg={useColorModeValue('white', 'gray.900')}
              borderColor={useColorModeValue('gray.200', 'gray.700')}>
              <MenuItem onClick={onOpen}>清空收藏</MenuItem>
            </MenuList>
          </Menu>
        </Flex>
      </HStack>
      <Modal isOpen={isOpen} onClose={onClose}>
        <ModalOverlay />
        <ModalContent>
          <ModalHeader>删除所有书签</ModalHeader>
          <ModalCloseButton />
          <ModalBody>
            <Text>【警告】删除后不可恢复!是否确定删除？</Text>
          </ModalBody>

          <ModalFooter>
            <Button colorScheme='red' mr={3} onClick={clearData}>
              确定
            </Button>
            <Button onClick={onClose}>取消</Button>
          </ModalFooter>
        </ModalContent>
      </Modal>
    </Flex>
  );
};
export default MobileNav;
