import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { solicitarRecuperacaoSenha, redefinirSenha } from '../services/authService'

function RecuperarSenha() {
  const navigate = useNavigate()
  const [step, setStep] = useState(1)
  const [tipoUsuario, setTipoUsuario] = useState('CLIENTE')
  const [email, setEmail] = useState('')
  const [form, setForm] = useState({ token: '', novaSenha: '', confirmacaoNovaSenha: '' })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  function handleFormChange(event) {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  async function handleSolicitarSubmit(event) {
    event.preventDefault()
    setError('')
    setLoading(true)

    try {
      await solicitarRecuperacaoSenha({ email, tipoUsuario })
      setStep(2)
    } catch (err) {
      setError(err.message || 'Não foi possível enviar o e-mail de recuperação.')
    } finally {
      setLoading(false)
    }
  }

  async function handleRedefinirSubmit(event) {
    event.preventDefault()
    setError('')

    if (form.novaSenha !== form.confirmacaoNovaSenha) {
      setError('As senhas não conferem.')
      return
    }

    setLoading(true)

    try {
      await redefinirSenha(form)
      setSuccess('Senha redefinida com sucesso! Você já pode fazer login.')
    } catch (err) {
      setError(err.message || 'Não foi possível redefinir a senha.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-16">
      <div className="mx-auto max-w-md">
        <div className="rounded-3xl bg-white p-8 shadow-sm md:p-10">

          {success ? (
            <div className="text-center">
              <div className="mb-4 text-5xl">✓</div>
              <h1 className="mb-2 text-2xl font-bold text-slate-900">Senha redefinida!</h1>
              <p className="mb-6 text-gray-500">{success}</p>
              <button
                onClick={() => navigate('/login')}
                className="w-full rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700"
              >
                Ir para o login
              </button>
            </div>
          ) : step === 1 ? (
            <>
              <div className="mb-8">
                <h1 className="mb-2 text-3xl font-bold text-slate-900">Esqueci minha senha</h1>
                <p className="text-gray-500">
                  Informe seu e-mail e enviaremos um token de recuperação.
                </p>
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

              <form className="space-y-5" onSubmit={handleSolicitarSubmit}>
                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-700">E-mail</label>
                  <input
                    type="email"
                    required
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    placeholder="seuemail@exemplo.com"
                    className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
                  />
                </div>

                <button
                  type="submit"
                  disabled={loading}
                  className="w-full rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-70"
                >
                  {loading ? 'Enviando...' : 'Enviar token de recuperação'}
                </button>
              </form>

              <p className="mt-6 text-center text-sm text-gray-500">
                Lembrou a senha?{' '}
                <Link to="/login" className="font-semibold text-blue-600 hover:underline">
                  Voltar ao login
                </Link>
              </p>
            </>
          ) : (
            <>
              <div className="mb-8">
                <h1 className="mb-2 text-3xl font-bold text-slate-900">Redefinir senha</h1>
                <p className="text-gray-500">
                  Verifique o token no console do servidor e defina sua nova senha.
                </p>
              </div>

              <div className="mb-6 rounded-2xl bg-blue-50 px-4 py-3 text-sm text-blue-700">
                Token de recuperação enviado para <strong>{email}</strong>. Insira-o abaixo.
              </div>

              {error ? (
                <div className="mb-6 rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
              ) : null}

              <form className="space-y-5" onSubmit={handleRedefinirSubmit}>
                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-700">Token de recuperação</label>
                  <input
                    type="text"
                    name="token"
                    required
                    value={form.token}
                    onChange={handleFormChange}
                    placeholder="Cole o token recebido por e-mail"
                    className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 font-mono text-sm outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
                  />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-700">Nova senha</label>
                  <input
                    type="password"
                    name="novaSenha"
                    required
                    value={form.novaSenha}
                    onChange={handleFormChange}
                    placeholder="Mínimo 6 caracteres"
                    className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
                  />
                </div>

                <div>
                  <label className="mb-2 block text-sm font-medium text-slate-700">Confirmar nova senha</label>
                  <input
                    type="password"
                    name="confirmacaoNovaSenha"
                    required
                    value={form.confirmacaoNovaSenha}
                    onChange={handleFormChange}
                    placeholder="Repita a nova senha"
                    className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
                  />
                </div>

                <div className="flex flex-col gap-3 pt-2 md:flex-row">
                  <button
                    type="button"
                    onClick={() => { setStep(1); setError('') }}
                    className="rounded-2xl border border-gray-300 px-6 py-3 font-medium text-slate-700 transition hover:bg-gray-50"
                  >
                    Voltar
                  </button>
                  <button
                    type="submit"
                    disabled={loading}
                    className="flex-1 rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-70"
                  >
                    {loading ? 'Redefinindo...' : 'Redefinir senha'}
                  </button>
                </div>
              </form>
            </>
          )}
        </div>
      </div>
    </div>
  )
}

export default RecuperarSenha
