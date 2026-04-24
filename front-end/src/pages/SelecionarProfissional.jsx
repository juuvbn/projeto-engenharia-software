import { useMemo, useState } from 'react'
import { Link, Navigate, useLocation, useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { criarPropostaInicial } from '../services/servicoService'
import { ESPECIALIDADES_LABELS } from '../utils/specialities'

function SelecionarProfissional() {
  const { prestadorId } = useParams()
  const location = useLocation()
  const navigate = useNavigate()
  const { isAuthenticated, isCliente } = useAuth()

  const prestador = location.state?.prestador
  const [descricao, setDescricao] = useState('')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  const especialidades = useMemo(
    () => (prestador?.especialidades || []).map((item) => ESPECIALIDADES_LABELS[item.especialidade] || item.especialidade),
    [prestador],
  )

  if (!prestador) {
    return (
      <div className="min-h-screen bg-[#f3f3f5] px-6 py-16">
        <div className="mx-auto max-w-2xl rounded-3xl bg-white p-10 text-center shadow-sm">
          <h1 className="text-3xl font-bold text-slate-900">Prestador não carregado</h1>
          <p className="mt-3 text-gray-600">Volte para a busca e selecione um prestador para iniciar a proposta.</p>
          <Link
            to="/buscar-servico"
            className="mt-6 inline-flex rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700"
          >
            Voltar para busca
          </Link>
        </div>
      </div>
    )
  }

  if (prestadorId && String(prestador.id) !== String(prestadorId)) {
    return <Navigate to="/buscar-servico" replace />
  }

  async function handleSubmit(event) {
    event.preventDefault()
    setError('')

    if (!isAuthenticated) {
      navigate('/login', {
        state: { returnTo: `/selecionar-profissional/${prestador.id}`, prestador },
      })
      return
    }

    if (!isCliente) {
      setError('Somente clientes podem criar proposta inicial de serviço.')
      return
    }

    if (!descricao.trim()) {
      setError('Descreva o que precisa ser feito para enviar a proposta.')
      return
    }

    setLoading(true)

    try {
      const servico = await criarPropostaInicial({
        prestadorId: prestador.id,
        descricao: descricao.trim(),
      })

      navigate('/confirmacao', {
        state: {
          servico,
          prestador,
          descricao: descricao.trim(),
        },
      })
    } catch (err) {
      setError(err.message || 'Não foi possível criar a proposta inicial.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-[#f3f3f5] pb-14">
      <section className="bg-blue-600 px-6 py-14 text-white">
        <div className="mx-auto max-w-6xl">
          <p className="text-sm font-semibold uppercase tracking-[0.22em] text-blue-100">HERO</p>
          <h1 className="mt-4 max-w-3xl text-4xl font-bold leading-tight md:text-5xl">
            Crie sua proposta inicial com {prestador.nome}
          </h1>
          <p className="mt-4 max-w-3xl text-lg text-blue-100">
            Explique com clareza o serviço desejado para que o prestador possa responder com data e orçamento.
          </p>
        </div>
      </section>

      <div className="mx-auto mt-8 grid max-w-6xl gap-6 px-6 lg:grid-cols-[1fr,1.2fr]">
        <aside className="rounded-3xl bg-white p-6 shadow-sm">
          <h2 className="text-2xl font-bold text-slate-900">Prestador selecionado</h2>
          <p className="mt-4 text-lg font-semibold text-slate-800">{prestador.nome}</p>
          <p className="mt-1 text-sm text-gray-500">{prestador.email}</p>
          <p className="mt-1 text-sm text-gray-500">{prestador.telefone}</p>

          <div className="mt-5 flex flex-wrap gap-2">
            {especialidades.map((item) => (
              <span key={item} className="rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700">
                {item}
              </span>
            ))}
          </div>
        </aside>

        <section className="rounded-3xl bg-white p-7 shadow-sm">
          <h2 className="text-2xl font-bold text-slate-900">Descreva sua proposta</h2>
          <p className="mt-2 text-gray-600">
            Informe os detalhes iniciais do atendimento. O prestador responderá com os próximos passos.
          </p>

          {error ? <div className="mt-5 rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div> : null}

          <form className="mt-6 space-y-5" onSubmit={handleSubmit}>
            <div>
              <label htmlFor="descricao" className="mb-2 block text-sm font-semibold text-slate-700">
                Descrição do serviço
              </label>
              <textarea
                id="descricao"
                rows={7}
                value={descricao}
                onChange={(event) => setDescricao(event.target.value)}
                placeholder="Ex.: Preciso de acompanhamento para idoso durante o período da tarde, com auxílio na medicação e alimentação."
                className="w-full rounded-2xl border border-gray-300 px-4 py-3 outline-none transition focus:border-blue-500 focus:ring-2 focus:ring-blue-200"
              />
            </div>

            <div className="flex flex-col gap-3 sm:flex-row">
              <button
                type="submit"
                disabled={loading}
                className="rounded-2xl bg-blue-600 px-6 py-3 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-70"
              >
                {loading ? 'Enviando proposta...' : 'Enviar proposta inicial'}
              </button>

              <Link
                to="/buscar-servico"
                className="rounded-2xl border border-gray-300 px-6 py-3 text-center font-semibold text-slate-700 transition hover:bg-gray-50"
              >
                Voltar para busca
              </Link>
            </div>
          </form>
        </section>
      </div>
    </div>
  )
}

export default SelecionarProfissional