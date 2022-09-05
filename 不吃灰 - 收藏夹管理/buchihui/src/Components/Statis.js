import {
  Box,
  Heading,
  Flex,
  SimpleGrid,
  Stat,
  StatLabel,
  StatNumber,
  Text,
} from '@chakra-ui/react';
import { MdBookmark, MdToday, MdUpdate, MdFavorite } from 'react-icons/md';
import dayjs from 'dayjs';
import LinkCard from './LinkCard';
// 数据统计组件
function StatsCard(props) {
  const { title, stat, icon } = props;
  return (
    <Stat
      px={{ base: 2, md: 4 }}
      py={'5'}
      shadow={'xl'}
      bgColor={'#fff'}
      rounded={'lg'}>
      <Flex justifyContent={'space-between'}>
        <Box pl={{ base: 2, md: 4 }}>
          <StatLabel fontWeight={'medium'}  fontSize={'16px'}>
            {title}
          </StatLabel>
          <StatNumber fontSize={'2xl'} fontWeight={'medium'} color={'gold'}>
            {stat}
          </StatNumber>
        </Box>
        <Box
          my={'auto'}
          color={'gray.800'}
          alignContent={'center'}>
          {icon}
        </Box>
      </Flex>
    </Stat>
  );
}

export default function Statis(props) {

  const { bookmarkData } = props;

  const bookmarkL = bookmarkData.length;
  let recentBookmark = [],startDay=1,favoriteNum=0,curDayBookmark=0;

  if (bookmarkL > 0) {
    recentBookmark = bookmarkL > 8 ? bookmarkData.slice(-8) : bookmarkData;
    startDay=dayjs(new Date()).diff(dayjs(bookmarkData[0].updatedAt),'days');
    favoriteNum=bookmarkData.filter((a)=>a.isFavorite===true)
    curDayBookmark=bookmarkData.filter((a)=>dayjs().isSame(a.updatedAt, 'day'))
  } else {
    recentBookmark = [];
  }

  return (
    <Box mx={'auto'} pt={5} px={{ base: 2, sm: 12, md: 17 }}>
      <Heading as='h5' mb={4}>数据统计</Heading>
      <SimpleGrid columns={[1, 2, 4]} spacing={{ base: 5, lg: 8 }}>
        <StatsCard
          title={'已使用天数'}
          stat={startDay}
          icon={<MdUpdate size={'2em'} />}
        />
        <StatsCard
          title={'书签总数'}
          stat={bookmarkL}
          icon={<MdBookmark size={'2em'} />}
        />
        <StatsCard
          title={'星标数量'}
          stat={favoriteNum.length||'0'}
          icon={<MdFavorite size={'2em'} />}
        />
        <StatsCard
          title={'今日收藏数'}
          stat={curDayBookmark.length||'0'}
          icon={<MdToday size={'2em'} />}
        />
      </SimpleGrid>

      <Heading as='h5' mt={4}>最近添加</Heading>
      {
        recentBookmark.length > 0 ?
          <LinkCard data={recentBookmark} /> :
          <Box align='center' size={'full'}>
            <Text mt={'10%'} fontSize={'20px'}>赶快点击右下角的按钮,添加链接吧 ↘</Text>
          </Box>
      }

    </Box>
  );
}
