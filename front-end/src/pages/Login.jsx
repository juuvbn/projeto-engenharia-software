import { useEffect, useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { loginCliente, loginPrestador } from '../services/authService'

function Login() {
  const navigate = useNavigate()
  const { isAuthenticated, login } = useAuth()
  const [tipoUsuario, setTipoUsuario] = useState('CLIENTE')
  const [form, setForm] = useState({ email: '', senha: '' })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    setError('')
  }, [tipoUsuario])

  if (isAuthenticated) {
    return <Navigate to="/perfil" replace />
  }

  function handleChange(event) {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setLoading(true)
    setError('')

    try {
      const response =
        tipoUsuario === 'PRESTADOR'
          ? await loginPrestador(form)
          : await loginCliente(form)

      login(response)
      navigate('/perfil')
    } catch (err) {
      setError(err.message || 'Não foi possível fazer login.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-16">
      <div className="mx-auto max-w-md">
        <div className="rounded-3xl bg-white p-8 shadow-sm md:p-10">
          <div className="mb-8 text-center">
            <h1 className="mb-2 text-4xl font-bold text-slate-900">Bem-vindo de volta</h1>
            <p className="text-gray-500">Acesse sua conta no Cuidar+</p>
          </div>

          <div className="mb-6 grid grid-cols-2 rounded-2xl bg-gray-100 p-1 text-sm font-medium">
            <button
              type="button"
              onClick={() => setTipoUsuario('CLIENTE')}
              className={`rounded-xl px-4 py-2 transition ${
                tipoUsuario === 'CLIENTE' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-500'
              }`}
            >
              Cliente
            </button>
            <button
              type="button"
              onClick={() => setTipoUsuario('PRESTADOR')}
              className={`rounded-xl px-4 py-2 transition ${
                tipoUsuario === 'PRESTADOR' ? 'bg-white text-blue-600 shadow-sm' : 'text-gray-500'
              }`}
            >
              Prestador
            </button>
          </div>

          {error ? (
            <div className="mb-6 rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
          ) : null}

          <form className="space-y-5" onSubmit={handleSubmit}>
            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">E-mail</label>
              <input
                type="email"
                name="email"
                value={form.email}
                onChange={handleChange}
                placeholder="seuemail@exemplo.com"
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">Senha</label>
              <input
                type="password"
                name="senha"
                value={form.senha}
                onChange={handleChange}
                placeholder="Digite sua senha"
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <button
              type="button"
              className="text-sm font-medium text-blue-600 hover:underline"
              onClick={() => navigate('/recuperar-senha')}
            >
              Esqueci minha senha
            </button>

            <button
              type="submit"
              disabled={loading}
              className="w-full rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-70"
            >
              {loading ? 'Entrando...' : 'Entrar'}
            </button>
          </form>

          <div className="my-6 flex items-center gap-3">
            <div className="h-px flex-1 bg-gray-200" />
            <span className="text-xs uppercase tracking-wider text-gray-400">ou</span>
            <div className="h-px flex-1 bg-gray-200" />
          </div>

          <div className="space-y-3 text-center text-sm text-gray-600">
            <p>
              Não tem conta?{' '}
              <Link to="/cadastro-cliente" className="font-semibold text-blue-600 hover:underline">
                Cadastrar como cliente
              </Link>
            </p>

            <p>
              Quer oferecer serviços?{' '}
              <Link to="/cadastro-prestador" className="font-semibold text-blue-600 hover:underline">
                Cadastro de prestador
              </Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Login