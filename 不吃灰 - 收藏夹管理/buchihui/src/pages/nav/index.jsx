import React, { useState, useEffect } from 'react';
import {
  Tabs, Tab, TabList, TabPanels, TabPanel, Box, Flex, Button, Input, Text,
  HStack, VStack, Container, Spacer, Image, SimpleGrid, Heading, useColorMode, Icon,
  Menu, MenuList, MenuItem, MenuButton, MenuDivider, Link,
  Portal,
} from '@chakra-ui/react';

import { useToggle, useClickAway } from 'react-use';
import { BiChevronDown } from 'react-icons/bi';
import { MdFavorite, MdMoreHoriz } from 'react-icons/md';

import listData from './webLink.json';
import CardRightMenuCopy from '../../Components/CardRightMenuCopy';
import ModalQRCode from '../../Components/ModalQRCode';
import { ArrowForwardIcon, ChevronUpIcon, ChevronDownIcon, ExternalLinkIcon } from '@chakra-ui/icons';

const cardStyleArr = [
  { type: 'list', name: '列表' },
  { type: 'title', name: '标题' },
  { type: 'link', name: '链接' },
];

export default function Nav()  {
  document.title = '我的主页';

  const [idx, setLikeIdx] = useState(0);
  const [siteIdx, setSiteIdx] = useState(0);//
  const [showDownList, setDropDown] = useToggle(false);// 选择下拉框
  const [inputVal, setQueryValue] = useState('');// 输入框查询值
  const [siteInfo, setSiteInfo] = useState(listData[0].list[0]);// 设置站点信息
  const [bookmarkList, setBookmarkList] = useState([]);
  const [cardStyle, setCardStyle] = useState('list');
  const [isQRCodeImg, setQRCodeInfo] = useState({});
  const navRef = React.useRef(null);
  //  const [value, setValue, remove] = useLocalStorage('my-key', 'foo');

  const categoryClick = (category, idx) => { // 类别按钮点击
    setLikeIdx(idx);
    setSiteIdx(0);
    setSiteInfo(listData[idx].list[0]);
    setDropDown(false);
  };

  const toggleSelect = () => {
    setDropDown(!showDownList);
  };

  const selectSite = (x, i) => {
    setSiteIdx(i);
    setSiteInfo(x);
    setDropDown(false);
  };

  const handleChange = (event) => setQueryValue(event.target.value);

  const toQuery = () => {
    let url = `${siteInfo.queryUrl}${inputVal}`;
    window.open(url, '_blank');
  };

  const enterDown = (e) => {
    if (e.key === 'Enter') {
      toQuery();
    }
  };
  const openLink = (link) => {
    window.open(link, '_blank');
  };

  useEffect(() => {
    setStorage();
  }, []);

  useClickAway(navRef, () => {
    setDropDown(false);
  });
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

  return (
    <Box>
      <Container px={6} py={6} maxW='2xl'>
        <Flex className='selectWrap' pt={'20px'} justifyContent={'space-evenly'}
              bgColor={'#eee'}
              maxWidth={1000}
              p={2}
        >
          <Box w={'140px'} bgColor={'gold'} pos='relative'
               p={2}
               pl={4}
               borderRadius={'12px'}
               alignSelf={'end'} mb={'16px'}>
            <HStack onClick={toggleSelect}>
              <Text w={'140px'} fontSize={'18px'}>
                {siteInfo.name}
              </Text>
              <Icon
                as={BiChevronDown}
                transition='all .25s ease-in-out'
                transform={showDownList ? 'rotate(180deg)' : ''}
              />
            </HStack>

            <VStack pos='absolute'
                    bgColor={'gold'}
                    borderRadius={'0 0 12px 12px'}
                    pt={'6px'}
                    ref={navRef}
                    top={'35px'} left={0}
                    zIndex={2}
                    alignItems={'flex-start'}>
              {showDownList && listData[idx].list.map((x, i) => {
                return (
                  <Box key={i} onClick={() => selectSite(x, i)}
                       w={'140px'}
                       m={0}
                       pl={2}
                       cursor='pointer'>
                    <Text fontSize={'18px'} color={'gray.600'}
                          p={2}
                          _hover={{
                            transform: 'translateX(4px)',
                            transition: '.3s',
                            color: '#000',
                            fontWeight: 'bold',
                          }}
                    >{x.name}</Text>
                  </Box>
                );
              })}
            </VStack>

          </Box>

          <Tabs variant='unstyled' ml={10}>
            <TabList>
              {listData.map((item, idx) => {
                return (
                  <Tab className='li' key={idx}
                       _selected={{ color: '#000', bg: 'gold', fontWeight: 'bold' }}
                       onClick={() => categoryClick(item.category, idx)}
                  >
                    {item.categoryName}
                  </Tab>
                );
              })}
            </TabList>
            <TabPanels>
              {listData.map((item, idx) => {
                const curCategory = listData[idx]?.list;
                const curSitePlaceholder = curCategory[siteIdx]?.name;
                return (
                  <TabPanel key={idx} pl={0}>
                    <HStack>
                      <Input type='text' placeholder={curSitePlaceholder}
                             onKeyDown={e => enterDown(e)}
                             bgColor={'#fff'}
                             data-query={siteInfo.queryUrl}
                             onChange={handleChange}
                             size='md'
                      />
                      <Button rightIcon={<ArrowForwardIcon />}
                              ml={10}
                              onClick={() => toQuery()}
                      >
                        搜索
                      </Button>
                    </HStack>
                  </TabPanel>
                );
              })}

            </TabPanels>
          </Tabs>
        </Flex>
      </Container>
      <Container ainer maxW='1200px'>
        <Flex mb={8}>
          <Heading as='h6'>我的收藏</Heading>
          <Spacer />
          <HStack>
            <span>格式：</span>
            {cardStyleArr.map((item, idx) => {
              return (
                <HStack key={idx}>
                  <Text key={idx}
                        color={item.type === cardStyle ? 'gold' : '#000'}
                        onClick={() => setCardStyle(item.type)}
                  >{item.name}</Text>
                  {idx !== cardStyleArr.length - 1 && <span>|</span>}
                </HStack>

              );
            })}
          </HStack>
        </Flex>
        <SimpleGrid columns={cardStyle === 'list' ? [1, null, 2, 3] : 1} spacing='40px' p={4}>
          {bookmarkList.length > 0 && bookmarkList.map((item, idx) => {
            if (cardStyle === 'list') {
              return (
                <Box key={idx}
                     display='flex'
                     overflow={'hidden'}
                     position={'relative'}
                     bgColor={'#eee'} p={5}
                     borderRadius='18px'
                     _hover={{
                       cursor: 'pointer',
                       outline: '1px solid #000',
                       boxShadow: '10px 10px 0 -4px gold, 10px 10px 0 0 black',
                       transition: '.3s ease-in-out',
                     }}
                >

                  {item.isFavorite &&
                    <Box position='absolute'
                         top='6px'
                         left='6px'>
                      <MdFavorite boxSize={10} color='gold' />
                    </Box>
                  }

                  <Menu>
                    <MenuButton
                      p={2}
                      position={'absolute'}
                      top={4} right={4}
                      as={Button}
                      float='right'
                      bgColor={'#fff'} ml={1}>
                      <Icon as={MdMoreHoriz} w='24px' h='24px' />
                    </MenuButton>
                    <Portal>
                      <MenuList border={'1px solid'}>
                        <MenuItem onClick={() => openLink(item.url)}>新标签页打开</MenuItem>
                        <MenuDivider />
                        <CardRightMenuCopy itemData={item} />
                        <MenuDivider />
                        <MenuItem onClick={() => setQRCodeInfo(item)}>生成二维码</MenuItem>
                      </MenuList>
                    </Portal>
                  </Menu>

                  <Box flexDirection='column' justifyContent='space-between' height={'100%'} display='flex'
                       onClick={() => openLink(item.url)}
                  >
                    <Text mb={4} fontSize='xl' fontWeight={'bold'} wordBreak={'break-word'} pr={'60px'}
                          className={'twoLine'} color={'#000'}
                    >{item.title}</Text>
                    <Text fontSize='sm' color={'#000'} className={'thirdLine'}
                    >{item.description}</Text>
                    <Image
                      mt={2}
                      borderRadius='18px'
                      maxWidth={'300px'}
                      maxHeight={'200px'}
                      src={item.image.length > 0 ? item.image : 'https://iph.href.lu/290x300?text=%E4%B8%8D%E5%90%83%E7%81%B0'}
                      alt={item.title}
                    />
                  </Box>
                </Box>
              );
            }

            if (cardStyle === 'link') {
              return (
                <Box>
                  <Link href={item.url} title={item.title} isExternal
                        _hover={{
                          cursor: 'pointer',
                          boxShadow: '0px 4px 0 0px gold, 0px 6px 0 0 black',
                        }}
                  >
                    {item.url} <ExternalLinkIcon mx='2px' />
                  </Link>
                </Box>
              );
            }

            if (cardStyle === 'title') {
              return (
                <Box bgColor={'#eee'} p={4} borderRadius='8px'>
                  <Text>
                    <Link href={item.url} title={item.url}
                          _hover={{
                            cursor: 'pointer',
                            boxShadow: '0px 4px 0 0px gold, 0px 6px 0 0 black',
                          }}
                    >{item.title}</Link>
                  </Text>
                </Box>
              );
            }
          })}
        </SimpleGrid>
      </Container>
      <ModalQRCode QRCodeInfo={isQRCodeImg} setQRCodeInfo={setQRCodeInfo} />
    </Box>
  );
};
