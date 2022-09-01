#!/usr/bin/env node

const esbuild = require('esbuild');
const {resolve} = require('path');

console.log('count-down.js started');
(async () => {
  try {
    await esbuild.build({
      entryPoints: [resolve(__dirname, '../sw/count-down.ts')],
      bundle: true,
      outfile: resolve(__dirname, '../public/count-down.js'),
      tsconfig: resolve(__dirname, '../tsconfig.json'),
      minify: true,
    });
    console.log('count-down.js built');
  } catch (e) {
    console.error(e);
    process.exit(1);
  }
})();
