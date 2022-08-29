/// <reference no-default-lib="true"/>
/// <reference lib="es2020" />
/// <reference lib="WebWorker" />

const sw = self as ServiceWorkerGlobalScope & typeof globalThis

sw.addEventListener('message', event => {
  console.log('worker received:', event.data);

  event.source.postMessage('hello');
});
