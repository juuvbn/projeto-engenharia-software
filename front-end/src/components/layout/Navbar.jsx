import { Link } from 'react-router-dom'

function Navbar() {
  const userType = 'cliente'; //simulado enquanto aguardamos endpoint do back-end
  
  return (
    <header className="w-full border-b border-gray-200 bg-white">
      <div className="mx-auto flex h-20 max-w-7xl items-center justify-between px-8">
        <Link to="/" className="flex items-center gap-3">
          <span className="text-4xl leading-none text-blue-600">♡</span>
          <span className="text-2xl font-bold text-blue-600">Cuidar+</span>
        </Link>

        <nav className="hidden items-center gap-10 md:flex">
          <Link to="/" className="font-semibold text-blue-600">
            Início
          </Link>
          <Link to="/buscar-servico" className="text-gray-500 hover:text-gray-800">
            Buscar
          </Link>
          <Link 
            to="/meus-servicos" 
            className={`font-medium transition-colors ${
              !userType 
                ? "text-gray-300 cursor-not-allowed pointer-events-none" // Desabilitado
                : "text-gray-500 hover:text-gray-800" // Ativo
            }`}
          >
          <Link to="/sobre" className="text-gray-500 hover:text-gray-800">
            Sobre
          </Link>
        </nav>

        <div className="flex items-center gap-4">
          <Link
            to="/login"
            className="rounded-2xl border border-blue-600 px-8 py-3 font-medium text-blue-600 transition hover:bg-blue-50"
          >
            Entrar
          </Link>
          <Link
            to="/cadastro"
            className="rounded-2xl bg-blue-600 px-8 py-3 font-semibold text-white transition hover:bg-blue-700"
          >
            Cadastrar
          </Link>
        </div>
      </div>
    </header>
  )
}

export default Navbar
