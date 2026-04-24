import { useState } from 'react'
import { Navigate, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { updateClientePassword, updatePrestadorPassword } from '../services/authService'

function AlterarSenha() {
  const navigate = useNavigate()
  const { isAuthenticated, isCliente, isPrestador } = useAuth()
  const [form, setForm] = useState({
    senhaAtual: '',
    novaSenha: '',
    confirmacaoNovaSenha: '',
  })
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [loading, setLoading] = useState(false)

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  function handleChange(event) {
    const { name, value } = event.target
    setForm((current) => ({ ...current, [name]: value }))
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')
    setSuccess('')

    if (form.novaSenha !== form.confirmacaoNovaSenha) {
      setError('A confirmação da nova senha não confere.')
      return
    }

    setLoading(true)

    try {
      const payload = {
        senhaAtual: form.senhaAtual,
        novaSenha: form.novaSenha,
        confirmacaoNovaSenha: form.confirmacaoNovaSenha,
      }

      if (isCliente) {
        await updateClientePassword(payload)
      } else if (isPrestador) {
        await updatePrestadorPassword(payload)
      }

      setSuccess('Senha atualizada com sucesso.')
      setForm({ senhaAtual: '', novaSenha: '', confirmacaoNovaSenha: '' })
      navigate('/perfil')
    } catch (err) {
      setError(err.message || 'Não foi possível alterar a senha.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-12">
      <div className="mx-auto max-w-xl rounded-3xl bg-white p-8 shadow-sm md:p-10">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-slate-900">Alterar senha</h1>
          <p className="mt-2 text-gray-500">Defina uma nova senha para sua conta.</p>
        </div>

        {error ? (
          <div className="mb-6 rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
        ) : null}

        {success ? (
          <div className="mb-6 rounded-2xl bg-green-50 px-4 py-3 text-sm text-green-700">{success}</div>
        ) : null}

        <form className="space-y-5" onSubmit={handleSubmit}>
          <div>
            <label className="mb-2 block text-sm font-medium text-slate-700">Senha atual</label>
            <input
              type="password"
              name="senhaAtual"
              value={form.senhaAtual}
              onChange={handleChange}
              className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium text-slate-700">Nova senha</label>
            <input
              type="password"
              name="novaSenha"
              value={form.novaSenha}
              onChange={handleChange}
              className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium text-slate-700">Confirmar nova senha</label>
            <input
              type="password"
              name="confirmacaoNovaSenha"
              value={form.confirmacaoNovaSenha}
              onChange={handleChange}
              className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
            />
          </div>

          <div className="flex flex-col gap-3 pt-4 md:flex-row">
            <button
              type="button"
              onClick={() => navigate('/perfil')}
              className="rounded-2xl border border-gray-300 px-6 py-3 font-medium text-slate-700 transition hover:bg-gray-50"
            >
              Cancelar
            </button>
            <button
              type="submit"
              disabled={loading}
              className="rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-70"
            >
              {loading ? 'Atualizando...' : 'Atualizar senha'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default AlterarSenha

