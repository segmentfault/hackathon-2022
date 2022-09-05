import React from 'react';
import {
  Flex,
  HStack, Image,
  SimpleGrid,
  Link,
  Heading,
  Text,
  Stack,
} from '@chakra-ui/react';
// 书签组件
export default function LinkCard({ data }) {

  return (
    <SimpleGrid spacing='40px' columns={[1, null, 2, null, 3, 4]} p={4}>
      {data.map((item, idx) => {
        return (
          <Flex key={idx}
                flexDirection={'column'}
                justifyContent={'space-between'}
                bg={'white'}
                boxShadow={'2xl'}
                borderRadius={'16px'}
                p={6}
                overflow={'hidden'}>
            <Image
              mt={2}
              borderRadius='18px'
              maxHeight={'130px'}
              outline={'1px solid #eee'}
              src={item.image.length > 0 ? item.image : 'https://iph.href.lu/290x130?text=%E4%B8%8D%E5%90%83%E7%81%B0'}
              alt={item.title}
            />
            <Stack>
              <Link href={item.url} isExternal>
                <Heading
                  color={'gray.700'}
                  fontSize={'xl'}
                  mt={4}
                  fontFamily={'body'}>
                  {item.title.slice(0, 20)}
                </Heading>
              </Link>
              <Text color={'gray.500'} fontSize={'md'}>
                {item.description.slice(0, 60)}
              </Text>
            </Stack>
            <HStack mt={6} direction={'column'} spacing={4} align={'center'}>
              <Stack direction={'column'} spacing={0} fontSize={'sm'}>
                <Text color={'gray.500'}>创建时间: {item.updatedAt}</Text>
                <Text color={'gray.500'} wordBreak={'break-all'}>{item.url}</Text>
              </Stack>
            </HStack>
          </Flex>
        );
      })}
    </SimpleGrid>
  );
}
