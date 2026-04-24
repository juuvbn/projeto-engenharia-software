import { useState } from 'react'
import { Link, Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { registerPrestador } from '../services/authService'
import { ESPECIALIDADES_OPTIONS } from '../utils/specialities'

function CadastroPrestador() {
  const navigate = useNavigate()
  const { isAuthenticated, login } = useAuth()
  const [form, setForm] = useState({
    nome: '',
    email: '',
    telefone: '',
    senha: '',
    confirmarSenha: '',
    especialidades: [],
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  if (isAuthenticated) {
    return <Navigate to="/perfil" replace />
  }

  function handleChange(event) {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  function handleEspecialidadeChange(value) {
    setForm((current) => {
      const alreadySelected = current.especialidades.includes(value)
      return {
        ...current,
        especialidades: alreadySelected
          ? current.especialidades.filter((item) => item !== value)
          : [...current.especialidades, value],
      }
    })
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')

    if (form.senha !== form.confirmarSenha) {
      setError('A confirmação da senha não confere.')
      return
    }

    if (form.especialidades.length === 0) {
      setError('Selecione pelo menos uma especialidade.')
      return
    }

    setLoading(true)

    try {
      const response = await registerPrestador({
        nome: form.nome,
        email: form.email,
        telefone: form.telefone,
        senha: form.senha,
        especialidades: form.especialidades,
      })

      login(response)
      navigate('/perfil')
    } catch (err) {
      setError(err.message || 'Não foi possível criar a conta.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-16">
      <div className="mx-auto max-w-2xl">
        <div className="rounded-3xl bg-white p-8 shadow-sm md:p-10">
          <div className="mb-8 text-center">
            <h1 className="mb-2 text-4xl font-bold text-slate-900">Cadastro de Prestador</h1>
            <p className="text-gray-500">Ofereça seus serviços no Cuidar+</p>
          </div>

          {error ? (
            <div className="mb-6 rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
          ) : null}

          <form className="space-y-5" onSubmit={handleSubmit}>
            <div className="grid gap-4 md:grid-cols-2">
              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">Nome completo</label>
                <input
                  name="nome"
                  value={form.nome}
                  onChange={handleChange}
                  className="w-full rounded-2xl border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
                />
              </div>

              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">Telefone</label>
                <input
                  name="telefone"
                  type="tel"
                  value={form.telefone}
                  onChange={handleChange}
                  placeholder="11999990000"
                  className="w-full rounded-2xl border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
                />
              </div>
            </div>

            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">E-mail</label>
              <input
                name="email"
                type="email"
                value={form.email}
                onChange={handleChange}
                placeholder="seuemail@exemplo.com"
                className="w-full rounded-2xl border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <div className="grid gap-4 md:grid-cols-2">
              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">Senha</label>
                <input
                  name="senha"
                  type="password"
                  value={form.senha}
                  onChange={handleChange}
                  className="w-full rounded-2xl border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
                />
              </div>

              <div>
                <label className="mb-2 block text-sm font-medium text-slate-700">Confirmar senha</label>
                <input
                  name="confirmarSenha"
                  type="password"
                  value={form.confirmarSenha}
                  onChange={handleChange}
                  className="w-full rounded-2xl border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
                />
              </div>
            </div>

            <div>
              <label className="mb-3 block text-sm font-medium text-slate-700">Especialidades</label>
              <div className="grid gap-3 md:grid-cols-2">
                {ESPECIALIDADES_OPTIONS.map((item) => (
                  <label
                    key={item.value}
                    className="flex cursor-pointer items-center gap-3 rounded-xl border border-gray-200 p-3 transition hover:bg-gray-50"
                  >
                    <input
                      type="checkbox"
                      checked={form.especialidades.includes(item.value)}
                      onChange={() => handleEspecialidadeChange(item.value)}
                    />
                    <span className="text-sm">{item.label}</span>
                  </label>
                ))}
              </div>
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-70"
            >
              {loading ? 'Cadastrando...' : 'Cadastrar como prestador'}
            </button>
          </form>

          <div className="mt-6 text-center text-sm text-gray-600">
            Já tem conta?{' '}
            <Link to="/login" className="font-semibold text-blue-600 hover:underline">
              Entrar
            </Link>
          </div>
        </div>
      </div>
    </div>
  )
}

export default CadastroPrestador