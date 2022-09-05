import { defineConfig, splitVendorChunkPlugin } from 'vite'
import vue from '@vitejs/plugin-vue'
import { VitePWA } from "vite-plugin-pwa";
import { resolve } from 'path';

// https://vitejs.dev/config/
export default defineConfig(({ command }) => {
  const isDev = command === 'serve';
  return {
    plugins: [
      vue(),
      splitVendorChunkPlugin(),
      VitePWA({
        registerType: 'autoUpdate',
        manifest: {
          name: '姆伊用药助手-好好吃药，有效吃药',
          short_name: '姆伊用药助手',
          description: '科学吃药，可以让药效发挥更好，副作用更少。慢性病患者献给病友的小应用。',
          start_url: "/?source=pwa",
          theme_color: '#ffe4b0',
          display: "standalone",
          scope: "/",
          orientation: "portrait",
          categories: [
            "工具",
            "辅助用药"
          ],
          icons: [
            {
              "src": "/icons/icon-72x72.png",
              "sizes": "72x72",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "/icons/icon-96x96.png",
              "sizes": "96x96",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "/icons/icon-128x128.png",
              "sizes": "128x128",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "/icons/icon-144x144.png",
              "sizes": "144x144",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "/icons/icon-152x152.png",
              "sizes": "152x152",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "/icons/icon-192x192.png",
              "sizes": "192x192",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "/icons/icon-384x384.png",
              "sizes": "384x384",
              "type": "image/png",
              "purpose": "any"
            },
            {
              "src": "/icons/icon-512x512.png",
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
