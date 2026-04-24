import { useEffect, useRef, useState } from 'react'
import { Navigate, Link } from 'react-router-dom'
import { ShieldCheck, BadgeInfo, Mail, User, MapPin, Phone } from 'lucide-react'
import { useAuth } from '../context/AuthContext'
import { getClienteProfile, getPrestadorProfile } from '../services/authService'
import { ESPECIALIDADES_LABELS } from '../utils/specialities'

function Perfil() {
  const { isAuthenticated, user, isCliente, isPrestador, updateUser } = useAuth()
  const [profile, setProfile] = useState(user)
  const [loading, setLoading] = useState(Boolean(user))
  const [error, setError] = useState('')
  const loadedProfileKeyRef = useRef('')

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
        setLoading(true)
        const response = isCliente ? await getClienteProfile() : await getPrestadorProfile()

        if (ignore) {
          return
        }

        const mappedProfile = isPrestador
          ? {
              ...response,
              especialidades: (response.especialidades || []).map((item) => item.especialidade),
            }
          : response

        setProfile(mappedProfile)
        updateUser({
          nome: response.nome,
          email: response.email,
        })
        loadedProfileKeyRef.current = profileKey
      } catch (err) {
        if (!ignore) {
          setError(err.message || 'Não foi possível carregar o perfil.')
        }
      } finally {
        if (!ignore) {
          setLoading(false)
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

  const tipoLabel = profile?.tipoUsuario === 'PRESTADOR' ? 'Prestador' : 'Cliente'
  const especialidades = profile?.especialidades || []

  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-12">
      <div className="mx-auto max-w-4xl">
        <div className="mb-8 rounded-3xl bg-white p-8 shadow-sm">
          <div className="flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
            <div>
              <p className="mb-2 inline-flex items-center gap-2 rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold uppercase tracking-wider text-blue-600">
                <ShieldCheck className="h-4 w-4" />
                Meu perfil
              </p>
              <h1 className="text-3xl font-bold text-slate-900">{profile?.nome || 'Carregando...'}</h1>
              <p className="mt-2 text-gray-500">{tipoLabel} autenticado</p>
            </div>

            <div className="flex flex-wrap gap-3">
              <Link
                to="/perfil/editar"
                className="rounded-2xl border border-blue-600 px-5 py-3 font-medium text-blue-600 transition hover:bg-blue-50"
              >
                Editar perfil
              </Link>
              <Link
                to="/perfil/senha"
                className="rounded-2xl bg-blue-600 px-5 py-3 font-semibold text-white transition hover:bg-blue-700"
              >
                Alterar senha
              </Link>
            </div>
          </div>
        </div>

        {error ? (
          <div className="mb-6 rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
        ) : null}

        <div className="grid gap-6 md:grid-cols-2">
          <section className="rounded-3xl bg-white p-8 shadow-sm">
            <div className="mb-6 flex items-center gap-3">
              <BadgeInfo className="h-5 w-5 text-blue-600" />
              <h2 className="text-xl font-bold text-slate-900">Dados da conta</h2>
            </div>

            <div className="space-y-4 text-sm">
              <div className="flex items-start gap-3 rounded-2xl bg-gray-50 p-4">
                <User className="mt-0.5 h-4 w-4 text-gray-500" />
                <div>
                  <p className="font-medium text-slate-900">Nome</p>
                  <p className="text-gray-600">{loading ? 'Carregando...' : profile?.nome}</p>
                </div>
              </div>

              <div className="flex items-start gap-3 rounded-2xl bg-gray-50 p-4">
                <Mail className="mt-0.5 h-4 w-4 text-gray-500" />
                <div>
                  <p className="font-medium text-slate-900">Email</p>
                  <p className="text-gray-600">{loading ? 'Carregando...' : profile?.email}</p>
                </div>
              </div>

              <div className="flex items-start gap-3 rounded-2xl bg-gray-50 p-4">
                <ShieldCheck className="mt-0.5 h-4 w-4 text-gray-500" />
                <div>
                  <p className="font-medium text-slate-900">Tipo de usuário</p>
                  <p className="text-gray-600">{profile?.tipoUsuario}</p>
                </div>
              </div>

              {isCliente ? (
                <div className="flex items-start gap-3 rounded-2xl bg-gray-50 p-4">
                  <MapPin className="mt-0.5 h-4 w-4 text-gray-500" />
                  <div>
                    <p className="font-medium text-slate-900">Endereço</p>
                    <p className="text-gray-600">{profile?.endereco || '-'}</p>
                  </div>
                </div>
              ) : null}
            </div>
          </section>

          <section className="rounded-3xl bg-white p-8 shadow-sm">
            <div className="mb-6 flex items-center gap-3">
              <MapPin className="h-5 w-5 text-blue-600" />
              <h2 className="text-xl font-bold text-slate-900">Ações rápidas</h2>
            </div>

            <div className="space-y-4 text-sm text-gray-600">
              <p>
                Use o menu lateral para atualizar seus dados pessoais e trocar sua senha.
              </p>
              <p>
                Todas as alterações do perfil passam pelos endpoints autenticados do back-end.
              </p>
              <div className="rounded-2xl border border-dashed border-gray-200 p-4">
                <div className="flex items-center gap-3">
                  <Phone className="h-4 w-4 text-gray-500" />
                  <span>Telefone e endereço ficam disponíveis na tela de edição.</span>
                </div>
              </div>

              {isPrestador && especialidades.length > 0 ? (
                <div className="rounded-2xl border border-gray-200 p-4">
                  <p className="mb-2 font-medium text-slate-900">Especialidades</p>
                  <div className="flex flex-wrap gap-2">
                    {especialidades.map((especialidade) => (
                      <span key={especialidade} className="rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700">
                        {ESPECIALIDADES_LABELS[especialidade] || especialidade}
                      </span>
                    ))}
                  </div>
                </div>
              ) : null}
            </div>
          </section>
        </div>
      </div>
    </div>
  )
}

export default Perfil
