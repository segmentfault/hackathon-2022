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
