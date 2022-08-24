export function diffMinutes(h: number, m: number, time: string): number {
  const [hour, minute] = time.split(':');
  const hourDiff = Number(hour) - h;
  const minuteDiff = Number(minute) - m;
  return hourDiff * 60 + minuteDiff;
}
