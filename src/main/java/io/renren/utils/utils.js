import httpRequest from '@/utils/httpRequest'
import {Message} from 'element-ui'

export function isEmpty(obj) {
  if (typeof obj === 'undefined' || obj == null || obj === '') {
    return true;
  } else {
    return false;
  }
}


export const POST=(url, data = {}) => {
  return new Promise(
    (resolve,reject)=>{
      httpRequest({
        url: httpRequest.adornUrl(url),
        method: 'post',
        data: httpRequest.adornData(data)
      }).then(({data}) => {
        debugger
        if (data && data.code === 0) {
            resolve(data);
        } else {
          Message({
            message: data.msg,
            type: 'warning',
          })
        }
      }).catch(error => {
        Message({
          message: '服务器开小差了请稍等一下',
          type: 'warning',
        })

      })
    }
  )

}
export const POSTArray=(url, data = {}) => {
  return new Promise(
    (resolve,reject)=>{
      httpRequest({
        url: httpRequest.adornUrl(url),
        method: 'post',
        data: data
      }).then(({data}) => {
        if (data && data.code === 0) {
            resolve(data);
        } else {
          Message({
            message: data.msg,
            type: 'warning',
          })

        }
      }).catch(error => {
        Message({
          message: '服务器开小差了请稍等一下',
          type: 'warning',
        })

      })
    }
  )

}
export  const  calcDic=(map,key, value)=>{
    let arr=map[key];
    //做兼容，多个残疾类别也可以拼装
    if (!this.isEmpty(value)&&typeof(value)== 'string' &&value.indexOf(",")>=0){
      let arryStr=value.split(",");
      let str="";
      for (let arryStrKey in arryStr) {
        for (let arrKey in arr) {
          if (arr[arrKey].dataNo === arryStr[arryStrKey]){
            if (this.isEmpty(str)){
              str+=arr[arrKey].dataName;
            }else {
              str=str+","+arr[arrKey].dataName;
            }
            break;
          }
        }
      }
      return str;
    }else if (!this.isEmpty(value)) {
      for (let arrKey in arr) {
        if (arr[arrKey].dataNo === value){
          return arr[arrKey].dataName;
        }
      }
    }
}
export const GET=(url, data = {}) => {

  return new Promise(
    (resolve,reject)=>{
      httpRequest({
        url: httpRequest.adornUrl(url),
        method: "get",
        params: httpRequest.adornParams(data)
      }).then(({ data }) => {
        if (data && data.code === 0) {
          resolve(data)
        } else {
          this.$message.error(data.msg);
        }
      });
    }
  ).catch(error=>{
    this.$message({
      message: '服务器开小差了请稍等一下',
      type: 'error',
      duration: 1500,
    })
  })

}
