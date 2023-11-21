function convertToDate(date: any) {
  if (isNaN(new Date(date).getTime())) {
    throw new Error(`${date} 不是一个有效的时间`);
  }
  if (!(date instanceof Date)) {
    date = new Date(date);
  }
  return date;
}

function formatDate(date: any, format = "yyyy-MM-dd hh:mm:ss"): string {
  date = convertToDate(date);
  const o = {
    "y+": date.getFullYear(),
    "M+": date.getMonth() + 1,
    "d+": date.getDate(),
    "h+": date.getHours(),
    "m+": date.getMinutes(),
    "s+": date.getSeconds(),
  };
  for (let k in o) {
    if (new RegExp(`(${k})`).test(format)) {
      format = format.replace(
        RegExp.$1,
        RegExp.$1.length === 1 ? o[k] : `0${o[k]}`.substr(-RegExp.$1.length)
      );
    }
  }
  return format;
}

function dateFormat(fmt: any, date: any) {
  let ret = "";
  date = new Date(date);
  const opt = {
    "Y+": date.getFullYear().toString(), // 年
    "m+": (date.getMonth() + 1).toString(), // 月
    "d+": date.getDate().toString(), // 日
    "H+": date.getHours().toString(), // 时
    "M+": date.getMinutes().toString(), // 分
    "S+": date.getSeconds().toString(), // 秒
    // 有其他格式化字符需求可以继续添加，必须转化成字符串
  };
  for (let k in opt) {
    let ret = new RegExp("(" + k + ")").exec(fmt);
    if (ret) {
      fmt = fmt.replace(
        ret[1],
        ret[1].length == 1 ? opt[k] : opt[k].padStart(ret[1].length, "0")
      );
    }
  }
  return fmt;
}

export { formatDate, dateFormat };
