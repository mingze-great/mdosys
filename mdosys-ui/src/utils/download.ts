// 下载
export function downLoad(res: any) {
  console.log(res.headers);
  let blob = res.data;
  let a = document.createElement("a");

  //由于后台返回文件名称是通过response返回的
  //因此需要从response headers中content-disposition响应头中获取文件名称fileName，如上图所示
  let headers = res.headers;
  let fileName = headers["content-disposition"];
  fileName = fileName.split("=")[1];

  //download是a标签的一个属性，可以自定义文件名称
  a.download = fileName;
  a.href = URL.createObjectURL(blob);
  document.body.appendChild(a);
  a.click();
  document.body.removeChild(a);
}
