import {
  Box, Drawer, DrawerContent, useDisclosure,
} from '@chakra-ui/react';
import { Link, matchRoutes, Outlet, useLocation } from 'react-router-dom';

import SidebarContent from './SidebarContent';
import MobileNav from './MobileNav';
import LinkCard from './Components/LinkCard';
import AddLinkBtn from './Components/AddLinkBtn';
import AddLinkModal from './Components/AddLinkModal';
import Statics from './Components/Statis';

import React, { useState, useRef, useEffect } from 'react';

const Layout = (props) => {
  const curPathname = props.path;

  const { isOpen, onOpen, onClose } = useDisclosure();
  const [addLinkState, setAddLinkState] = useState(false);
  const [bookmarkList, setBookmarkList] = useState([]);

  useEffect(() => {
    setStorage();
  }, []);

  const setStorage = () => {

    let getStoreData = localStorage.getItem('bookmarkData');
    if (getStoreData === undefined || getStoreData === null) {
      let bookmarkData = {
        list: [],
      };
      localStorage.setItem('bookmarkData', JSON.stringify(bookmarkData));
    } else {
      let storeData = JSON.parse(getStoreData);
      setBookmarkList(storeData.list);
    }
  };

  useEffect(() => {
    setBookmarkList(JSON.parse(localStorage.getItem('bookmarkData')).list || []);
  }, [JSON.stringify(localStorage.getItem('bookmarkData'))]);

  const closeAddLinkModal = () => {
    setAddLinkState(false);
  };

  return (
    <Box>

      <SidebarContent
        display={{ sm: 'none', xl: 'block' }}
        onClose={() => onClose}
      />

      <AddLinkModal
        isOpen={addLinkState}
        onClose={closeAddLinkModal}
      />

      <Drawer
        autoFocus={false}
        isOpen={isOpen}
        placement='left'
        onClose={onClose}
        returnFocusOnClose={false}
        onOverlayClick={onClose}
        size='full'
      >
        <DrawerContent>
          <SidebarContent onClose={onClose} />
        </DrawerContent>
      </Drawer>

      <Box
        float='right'
        minHeight='100vh'
        height='100%'
        overflow='auto'
        position='relative'
        maxHeight='100%'
        w={{ base: '100%', xl: 'calc( 100% - 340px )' }}
        maxWidth={{ base: '100%', xl: 'calc( 100% - 340px )' }}
        transition='all 0.33s cubic-bezier(0.685, 0.0473, 0.346, 1)'
        transitionDuration='.2s, .2s, .35s'
        transitionProperty='top, bottom, width'
        transitionTimingFunction='linear, linear, ease'
      >
        <MobileNav isHShow={onOpen} data={bookmarkList} updateBookmak={setBookmarkList} />
        <Box
          mt='120px'
          h={'calc(100vh - 120px)'}
          bgColor={'#eee'}
          overflowY={'auto'}
        >
          {
            curPathname === 'bookmarkList' &&
            bookmarkList.length > 0 && <LinkCard data={bookmarkList} />
          }
          {curPathname === 'index' && <Statics bookmarkData={bookmarkList} />          }
          {/*{curPathname === 'nav' && <Nav />          }*/}
        </Box>
        <AddLinkBtn addLink={() => setAddLinkState(true)} />
      </Box>
    </Box>
  );
};

export default Layout;
