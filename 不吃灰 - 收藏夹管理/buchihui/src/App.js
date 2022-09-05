import React from 'react';
import { theme, ChakraProvider } from '@chakra-ui/react';

import { BrowserRouter, Routes, Route } from 'react-router-dom';

import Layout from './Layout';
import Nav from './pages/nav';


export default function App() {
  return (
    <ChakraProvider theme={theme}>
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Layout path='index' />} />
          <Route path='/bookmarkList' element={<Layout path='bookmarkList' />} />
          <Route path='/nav' element={<Nav />} />
        </Routes>
      </BrowserRouter>,
    </ChakraProvider>
  );
}
