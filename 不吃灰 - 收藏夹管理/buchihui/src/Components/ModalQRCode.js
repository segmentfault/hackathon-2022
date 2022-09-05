import {Text,  VStack,
  Modal, ModalBody, ModalContent, ModalHeader, ModalOverlay, ModalCloseButton
} from '@chakra-ui/react';
import { QRCodeCanvas } from 'qrcode.react';
import React from 'react';
//二维码组件
export default function ModalQrCode({ QRCodeInfo,setQRCodeInfo }){

  return(
    <Modal isOpen={Object.keys(QRCodeInfo).length > 0} onClose={()=>setQRCodeInfo({})}>
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>生成二维码</ModalHeader>
        <ModalCloseButton />
        <ModalBody>
          <VStack pb={4}>
            <Text>{QRCodeInfo.title}</Text>
            <QRCodeCanvas value={QRCodeInfo?.url} />
          </VStack>
        </ModalBody>
      </ModalContent>
    </Modal>
  )
}
