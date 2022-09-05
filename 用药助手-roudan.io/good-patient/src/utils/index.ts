export function diffMinutes(h: number, m: number, time: string): number {
  const [hour, minute] = time.split(':');
  const hourDiff = Number(hour) - h;
  const minuteDiff = Number(minute) - m;
  let result = hourDiff * 60 + minuteDiff;
  // 需要计算到下一天
  if (result > -1380) return result;
  return result + 1440;
}
