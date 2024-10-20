import html2canvas from "html2canvas";
import jsPdf from "jspdf";

async function htmlToPdf(title) {
  const elId = 'pdfContent' // body: 想要生成 pdf 的页面的id
  if (!elId) {
    // tslint:disable-next-line:no-console
    console.error('导出节点不存在！')
    return
  }
  // 将html dom节点生成canvas
  const htmlCanvas = await getCanvasByHtmlId(elId)
  // 将canvas对象转为pdf
  const pdf = canvasToPdf(htmlCanvas)
  // 通过浏览器下载pdf
  downPdf(pdf, title)
}

/**
 *  @param elId 打印的节点ID
 */
async function getCanvasByHtmlId(elId) {
  const elIdDom  = document.getElementById(elId)
  const canvas  = await html2canvas(elIdDom, {
    scale: 2,
    useCORS: true,
    allowTaint: true,
    // taintTest: false,
    imageTimeout: 0,
  }).then((canvas) => {
    return canvas
  })

  return canvas
}

/**
 *  @param htmlCanvas canvas对象
 */
function canvasToPdf(htmlCanvas) {
  const canvasWidth = htmlCanvas.width
  const canvasHeight = htmlCanvas.height
  const imgBase64 = htmlCanvas.toDataURL('image/jpeg', 1.0)

  // a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
  const imgWidth = 595.28
  // 图片高度需要等比缩放
  const imgHeight = 595.28 / canvasWidth * canvasHeight

  let pageHeight = imgHeight // pdf转化后页面总高度
  let position = 0

  const pdfInstance = new jsPdf('', 'pt', 'a4')
  pdfInstance.setFontSize(12)

  if (imgHeight < 841.89) {
    pdfInstance.addImage(imgBase64, 'JPEG', 0, 0, imgWidth, imgHeight)
  } else {
    while (pageHeight > 0) {
      pdfInstance.addImage(imgBase64, 'JPEG', 0, position, imgWidth, imgHeight)
      pageHeight -= 841.89
      position -= 841.89
      if (pageHeight > 0) {
        pdfInstance.addPage()
      }
    }
  }

  return pdfInstance
}

function downPdf(pdfInstance, title) {
  // 文件名过长导致下载失败
  if (title.length > 50) {
    title = title.substring(title.length - 50)
  }

  pdfInstance.save(title + '.pdf', { returnPromise: true }).then(() => {
    // 搜狗浏览器下载机制问题暂时不关闭
    if (!(navigator.userAgent.toLowerCase().indexOf('se 2.x') > -1)) {
      setTimeout(window.close, 300)
    }
  })
}

export default htmlToPdf
