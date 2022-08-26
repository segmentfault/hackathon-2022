export async function checkPermission(): Promise<boolean> {
  let {permission} = Notification;
  switch (permission) {
    case "granted": return true;
    case "denied": return false;
  }
  permission = await Notification.requestPermission();
  return permission === "granted";
}

let registration: ServiceWorkerRegistration | undefined;
export async function registerServiceWorker(): Promise<void> {
  try {
    registration = await navigator.serviceWorker.register("/count-down.js", {
      scope: "/",
    });
    navigator.serviceWorker.addEventListener('message', event => {
      console.log('from worker:', event.data);
    });

    registration = await navigator.serviceWorker.ready;
  } catch (error) {
    console.error(`Registration failed with ${error}`);
  }
}

export async function sendMessageAfter(seconds: number, title: string, content: string): Promise<void> {
  console.log('xxxs');
  setTimeout(async () => {
    console.log('ooo', title, content, !!registration);
    try {
      const registration = await navigator.serviceWorker.ready;
      await registration.showNotification(title, {
        body: content,
      });
      console.log('done');
    } catch (e) {
      console.error(e);
    }
  }, seconds * 1000);
}
