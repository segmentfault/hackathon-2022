export async function checkPermission(): Promise<boolean> {
  let {permission} = Notification;
  switch (permission) {
    case "granted": return true;
    case "denied": return false;
  }
  permission = await Notification.requestPermission();
  return permission === "granted";
}

export async function registerServiceWorker(): Promise<void> {
  try {
    await navigator.serviceWorker.register("/count-down.js", {
      scope: "/",
    });
    navigator.serviceWorker.addEventListener('message', event => {
      console.log('from worker:', event.data);
    });
  } catch (error) {
    console.error(`Registration failed with ${error}`);
  }
}

export async function sendMessageAfter(seconds: number, title: string, content: string): Promise<void> {
  try {
    const registration = await navigator.serviceWorker.ready;
    console.log('note at:', new Date(Date.now() + seconds * 1000))
    await registration.showNotification(title, {
      body: content,
      timestamp: Date.now() + seconds * 1000,
    });
    console.log('done');
  } catch (e) {
    console.error(e);
  }
}
