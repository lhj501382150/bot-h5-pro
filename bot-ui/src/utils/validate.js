let url = /^http[s]?:\/\/.*/
let regId = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
let number = /^\d+(\.\d+)?$/
let email = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/    //邮箱
let tel = /^1[345789]\d{9}$/        //手机号码
let fax = /^(\d{3,4}-)?\d{7,8}$/    //传真
let name = /^[\u4E00-\u9FA5\uf900-\ufa2d·s]{2,10}$/;   //姓名
let FormValidate = (function () {
  // eslint-disable-next-line
  function FormValidate() {}
  // From表单验证规则  可用于公用的校验部分
  FormValidate.Form = function () {
    return {
      // 姓名的验证规则
      Name: function (rule, value, callback) {
        if (!value) {
          return callback(new Error('姓名不能为空'))
        }
        if (!name.test(value)) {
          callback(new Error('请输入正确姓名!'))
        } else {
          callback()
        }
      },
      // 邮箱的正则
      Email: function (rule, value, callback) {
        if (!value) {
          return callback(new Error('邮箱不能为空'))
        }
        if (!email.test(value)) {
          callback(new Error('请输入正确的邮箱!'))
        } else {
          callback()
        }
      },
      // 身份证的验证规则
      ID: function (rule, value, callback) {
        if (!value) {
          return callback(new Error('身份证不能为空'))
        }
        if (!regId.test(value)) {
          callback(new Error('请输入正确的二代身份证号码'))
        } else {
          callback()
        }
      },
      // 电话号码的验证
      Tel: (rule, value, callback) => {
        if (!value) {
          return callback(new Error('电话不能为空'))
        }
        if (!tel.test(value)) {
          callback(new Error('请正确填写电话号码'))
        } else {
          callback()
        }
      },
      Number:(rule, value, callback) => {
        if (value!= 0 && !value) {
          return callback(new Error('不能为空'))
        }
        if (!number.test(value)) {
          callback(new Error('请输入数字'))
        } else {
          callback()
        }
      }

    }
  }

  // FromOne表单验证规则  用于FromOne个性化的校验
  FormValidate.FormOne = function () {
    return {
      // 姓名的验证规则
      Name: function (rule, value, callback) {
        if (!value) {
          return callback(new Error('姓名不能为空'))
        }
        if (!name.test(value)) {
          callback(new Error('请输入正确姓名!'))
        } else {
          callback()
        }
      }
    }
  }
  return FormValidate
}())
exports.FormValidate = FormValidate
