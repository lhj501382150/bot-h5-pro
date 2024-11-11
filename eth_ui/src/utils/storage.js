export function setStrorage(key,data){
  const type = typeof data
  //const value = type === 'string' ? data : JSON.stringify(data)
  try{
    sessionStorage.setItem(key,data)
  }catch(e){
    console.log('setStrorage error',e)
  }
}

export function getStrorage(key){
  let value = sessionStorage && sessionStorage.getItem(key)
  return value
}

export function removeStrorage(key){
  sessionStorage && sessionStorage.removeItem(key)
}

export function clearStrorage(){
  sessionStorage && sessionStorage.clear()
}
