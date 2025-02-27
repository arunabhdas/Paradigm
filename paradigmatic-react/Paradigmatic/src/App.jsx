import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <div class="dark:bg-gray-800">
      <h1 className='text-5xl text-center dark:text-blue-500'>Welcome to Paradigmatic</h1>
      <h1 class="text-3xl font-bold underline dark:text-red-500">
        Hello world!
      </h1>
      </div>
    </>
  )
}

export default App
