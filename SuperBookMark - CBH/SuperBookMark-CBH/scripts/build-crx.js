const AdmZip = require('adm-zip');
const zip = new AdmZip();
zip.addLocalFolder('crx');
zip.writeZip('dist/SuperBookMark.crx');
zip.writeZip('dist/SuperBookMark.zip');
console.log('build crx file success!');
