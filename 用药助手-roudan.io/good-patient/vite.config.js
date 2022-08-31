import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { VitePWA } from "vite-plugin-pwa";
import { resolve } from 'path';

// https://vitejs.dev/config/
export default defineConfig(({ command }) => {
  const isDev = command === 'serve';
  return {
    plugins: [
      vue(),
      VitePWA({
        registerType: 'autoUpdate',
        manifest: {
          name: '姆伊用药助手',
          short_name: 'mudicine',
          description: '更科学的用药助手',
          theme_color: '#FFFFFF',
          icons: [
            {
              "src": "assets/icons/icon-72x72.png",
              "sizes": "72x72",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "assets/icons/icon-96x96.png",
              "sizes": "96x96",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "assets/icons/icon-128x128.png",
              "sizes": "128x128",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "assets/icons/icon-144x144.png",
              "sizes": "144x144",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "assets/icons/icon-152x152.png",
              "sizes": "152x152",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "assets/icons/icon-192x192.png",
              "sizes": "192x192",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "assets/icons/icon-384x384.png",
              "sizes": "384x384",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "assets/icons/icon-512x512.png",
              "sizes": "512x512",
              "type": "image/png",
              "purpose": "any"
            }
          ]
        },
      }),
    ],
    resolve: {
      alias: {
        '@': resolve(__dirname, './src'),
      },
    },
  };
});
