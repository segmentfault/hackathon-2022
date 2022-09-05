import { Box, CloseButton, Flex, Icon, Text, Link, useColorModeValue } from '@chakra-ui/react';
import React from 'react';
import { MdBookmark ,MdHome,MdDashboard} from 'react-icons/md';
import { Link as ReactLink } from 'react-router-dom';
import {  useNavigate } from 'react-router-dom';

// 侧边栏
const LinkItems = [
  { name: 'Dashboard', icon: MdDashboard, path: '/' },
  { name: '所有书签', icon: MdBookmark, path: '/bookmarkList' },
  { name: '我的主页', icon: MdHome, path: '/nav' },
];

const NavItem = ({ icon, children, ...rest }) => {
  return (
    <Link
          style={{ textDecoration: 'none' }}
          _focus={{ boxShadow: 'none' }}
    >
      <Flex
        align='center'
        p='4'
        mx='4'
        borderRadius='lg'
        role='group'
        cursor='pointer'
        _hover={{
          bg: '#F1A661',
          color: '#000',
        }}
        {...rest}>
        {icon && (
          <Icon
            mr='4'
            fontSize='16'
            as={icon}
          />
        )}
        {children}
      </Flex>
    </Link>
  );
};

const SidebarContent = ({ onClose, ...rest }) => {
  let navigate = useNavigate();
  return (
    <Box
      bg={'#fff'}
      w='300px'
      h='100vh'
      m={'0px'}
      position='fixed'
      minH='100%'
      overflowX='hidden'
      filter='drop-shadow(0px 5px 14px rgba(0, 0, 0, 0.05))'
      ms={{ sm: '16px' }}
      my={{ sm: '16px' }}
      borderRadius='25px'
      boxShadow='0 0 5px rgba(0,0.4)'
      {...rest}>
      <Box
        h='calc(100vh - 32px)'
        borderRadius='25px'
        boxShadow='0 0 5px rgba(0,0.4)'
        bg={'#FFD8A9'}
      >

        <Flex h='20' alignItems='center' mx='8' justifyContent='space-between' textAlign='center'>
          <Text fontSize='2xl' fontFamily='monospace' fontWeight='bold' w='full'>
            不吃灰
          </Text>
          <CloseButton display={{ base: 'flex', lg: 'none' }} onClick={onClose} />
        </Flex>

        {LinkItems.map((link) => (
          <NavItem key={link.name} icon={link.icon} onClick={() => navigate(link.path)}
          >
            <Link as={ReactLink} to={`${link.path}`}>
              {link.name}
            </Link>

          </NavItem>
        ))}
      </Box>
    </Box>
  );
};

export default SidebarContent;
