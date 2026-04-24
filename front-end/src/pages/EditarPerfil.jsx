import { useEffect, useMemo, useRef, useState } from 'react'
import { Navigate, useNavigate } from 'react-router-dom'
import { ESPECIALIDADES_OPTIONS } from '../utils/specialities'
import { useAuth } from '../context/AuthContext'
import { getClienteProfile, getPrestadorProfile, updateClienteProfile, updatePrestadorProfile } from '../services/authService'

function EditarPerfil() {
  const navigate = useNavigate()
  const { isAuthenticated, isCliente, isPrestador, user, updateUser } = useAuth()
  const [loading, setLoading] = useState(false)
  const [loadingProfile, setLoadingProfile] = useState(Boolean(user))
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const loadedProfileKeyRef = useRef('')
  const [form, setForm] = useState({
    nome: '',
    telefone: '',
    email: '',
    endereco: '',
    especialidades: [],
  })

  const title = useMemo(() => {
    return isPrestador ? 'Editar perfil de prestador' : 'Editar perfil de cliente'
  }, [isPrestador])

  useEffect(() => {
    let ignore = false
    const profileKey = user ? `${user.id}:${user.tipoUsuario}` : 'anonymous'

    if (loadedProfileKeyRef.current === profileKey) {
      return () => {
        ignore = true
      }
    }

    async function loadProfile() {
      if (!isAuthenticated) {
        return
      }

      try {
        setLoadingProfile(true)
        const response = isCliente ? await getClienteProfile() : await getPrestadorProfile()

        if (ignore) {
          return
        }

        setForm({
          nome: response.nome || '',
          telefone: response.telefone || '',
          email: response.email || '',
          endereco: response.endereco || '',
          especialidades: (response.especialidades || []).map((item) => item.especialidade),
        })

        updateUser({
          nome: response.nome,
          email: response.email,
        })
        loadedProfileKeyRef.current = profileKey
      } catch (err) {
        if (!ignore) {
          setError(err.message || 'Não foi possível carregar os dados do perfil.')
        }
      } finally {
        if (!ignore) {
          setLoadingProfile(false)
        }
      }
    }

    loadProfile()

    return () => {
      ignore = true
    }
  }, [isAuthenticated, isCliente, isPrestador, updateUser, user])

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />
  }

  function handleChange(event) {
    const { name, value } = event.target
    setForm((current) => ({
      ...current,
      [name]: value,
    }))
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
    setSuccess('')
    setLoading(true)

    try {
      const payload = {
        nome: form.nome,
        telefone: form.telefone,
        email: form.email,
      }

      let response
      if (isCliente) {
        response = await updateClienteProfile({
          ...payload,
          endereco: form.endereco,
        })
      } else {
        response = await updatePrestadorProfile({
          ...payload,
          especialidades: form.especialidades,
        })
      }

      updateUser({
        nome: response.nome,
        email: response.email,
      })

      setSuccess('Perfil atualizado com sucesso.')
      navigate('/perfil')
    } catch (err) {
      setError(err.message || 'Não foi possível atualizar o perfil.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-12">
      <div className="mx-auto max-w-3xl rounded-3xl bg-white p-8 shadow-sm md:p-10">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-slate-900">{title}</h1>
          <p className="mt-2 text-gray-500">Atualize seus dados pessoais e mantenha seu cadastro em dia.</p>
        </div>

        {error ? (
          <div className="mb-6 rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
        ) : null}

        {success ? (
          <div className="mb-6 rounded-2xl bg-green-50 px-4 py-3 text-sm text-green-700">{success}</div>
        ) : null}

        {loadingProfile ? (
          <div className="mb-6 rounded-2xl bg-gray-50 px-4 py-3 text-sm text-gray-600">Carregando dados do perfil...</div>
        ) : null}

        <form className="space-y-5" onSubmit={handleSubmit}>
          <div>
            <label className="mb-2 block text-sm font-medium text-slate-700">Nome completo</label>
            <input
              name="nome"
              value={form.nome}
              onChange={handleChange}
              className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium text-slate-700">E-mail</label>
            <input
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
            />
          </div>

          <div>
            <label className="mb-2 block text-sm font-medium text-slate-700">Telefone</label>
            <input
              name="telefone"
              value={form.telefone}
              onChange={handleChange}
              placeholder="11999990000"
              className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
            />
          </div>

          {isCliente ? (
            <div>
              <label className="mb-2 block text-sm font-medium text-slate-700">Endereço</label>
              <input
                name="endereco"
                value={form.endereco}
                onChange={handleChange}
                className="w-full rounded-2xl border border-gray-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>
          ) : null}

          {isPrestador ? (
            <div>
              <label className="mb-3 block text-sm font-medium text-slate-700">Especialidades</label>
              <div className="grid gap-3 md:grid-cols-2">
                {ESPECIALIDADES_OPTIONS.map((item) => (
                  <label
                    key={item.value}
                    className="flex cursor-pointer items-center gap-3 rounded-2xl border border-gray-200 p-4 transition hover:bg-gray-50"
                  >
                    <input
                      type="checkbox"
                      checked={form.especialidades.includes(item.value)}
                      onChange={() => handleEspecialidadeChange(item.value)}
                    />
                    <span className="text-sm font-medium text-slate-700">{item.label}</span>
                  </label>
                ))}
              </div>
            </div>
          ) : null}

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
              {loading ? 'Salvando...' : 'Salvar alterações'}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default EditarPerfil
