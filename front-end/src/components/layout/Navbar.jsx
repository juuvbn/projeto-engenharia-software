import { Link } from 'react-router-dom'

function Navbar() {
  return (
    <nav className="bg-blue-600 text-white px-6 py-4 flex justify-between items-center">
      <h1 className="text-xl font-bold">Cuidar+</h1>

      <div className="space-x-4">
        <Link to="/" className="hover:underline">Home</Link>
        <Link to="/sobre" className="hover:underline">Sobre</Link>
        <Link to="/login" className="hover:underline">Login</Link>
        <Link to="/cadastro" className="hover:underline">Cadastro</Link>
      </div>
    </nav>
  )
}

export default Navbar