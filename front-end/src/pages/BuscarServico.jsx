import { useEffect, useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import ProfissionalCard from '../components/service/ProfissionalCard'
import { useAuth } from '../context/AuthContext'
import { listarPrestadores } from '../services/prestadorService'
import { ESPECIALIDADES_OPTIONS } from '../utils/specialities'

const PAGE_SIZE = 12

function BuscarServico() {
  const navigate = useNavigate()
  const { isAuthenticated, isCliente, isPrestador, user } = useAuth()
  const [especialidadesSelecionadas, setEspecialidadesSelecionadas] = useState([])
  const [page, setPage] = useState(0)
  const [result, setResult] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const totalPages = result?.totalPages || 0
  const prestadores = result?.content || []

  const filtrosAtivosTexto = useMemo(() => {
    if (especialidadesSelecionadas.length === 0) {
      return 'Todas as especialidades'
    }

    return `${especialidadesSelecionadas.length} filtro(s) aplicado(s)`
  }, [especialidadesSelecionadas])

  useEffect(() => {
    let isCancelled = false

    async function carregarPrestadores() {
      setLoading(true)
      setError('')

      try {
        const payload = await listarPrestadores({
          especialidades: especialidadesSelecionadas,
          page,
          size: PAGE_SIZE,
        })

        if (!isCancelled) {
          setResult(payload)
        }
      } catch (err) {
        if (!isCancelled) {
          setError(err.message || 'Não foi possível carregar os prestadores.')
          setResult(null)
        }
      } finally {
        if (!isCancelled) {
          setLoading(false)
        }
      }
    }

    carregarPrestadores()

    return () => {
      isCancelled = true
    }
  }, [especialidadesSelecionadas, page])

  function toggleEspecialidade(value) {
    setPage(0)
    setEspecialidadesSelecionadas((current) =>
      current.includes(value) ? current.filter((item) => item !== value) : [...current, value],
    )
  }

  function limparFiltros() {
    setPage(0)
    setEspecialidadesSelecionadas([])
  }

  function handleSelectPrestador(prestador) {
    if (!isAuthenticated) {
      navigate('/login', {
        state: {
          returnTo: `/selecionar-profissional/${prestador.id}`,
          prestador,
        },
      })
      return
    }

    if (!isCliente) {
      return
    }

    navigate(`/selecionar-profissional/${prestador.id}`, {
      state: {
        prestador,
      },
    })
  }

  return (
    <div className="min-h-screen bg-[#f3f3f5] px-6 py-10">
      <div className="mx-auto max-w-7xl">
        <header className="mb-8 rounded-3xl bg-white p-8 shadow-sm">
          <p className="text-xs font-semibold uppercase tracking-[0.22em] text-gray-500">Busca de prestadores</p>
          <h1 className="mt-3 text-4xl font-bold text-slate-900">Encontre profissionais por especialidade</h1>
          <p className="mt-3 text-gray-600">
            Selecione uma ou mais especialidades para filtrar os prestadores cadastrados.
          </p>
        </header>

        <section className="mb-8 rounded-3xl bg-white p-6 shadow-sm">
          <div className="mb-4 flex items-center justify-between gap-3">
            <h2 className="text-lg font-semibold text-slate-900">Filtros</h2>
            <button
              type="button"
              onClick={limparFiltros}
              className="text-sm font-semibold text-blue-600 transition hover:text-blue-700"
            >
              Limpar filtros
            </button>
          </div>

          <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-4">
            {ESPECIALIDADES_OPTIONS.map((option) => {
              const checked = especialidadesSelecionadas.includes(option.value)

              return (
                <label
                  key={option.value}
                  className={`flex cursor-pointer items-center gap-3 rounded-2xl border px-4 py-3 transition ${
                    checked
                      ? 'border-blue-500 bg-blue-50 text-blue-800'
                      : 'border-gray-200 bg-white text-slate-700 hover:border-gray-300'
                  }`}
                >
                  <input
                    type="checkbox"
                    checked={checked}
                    onChange={() => toggleEspecialidade(option.value)}
                    className="h-4 w-4 rounded border-gray-300 text-blue-600"
                  />
                  <span className="text-sm font-medium">{option.label}</span>
                </label>
              )
            })}
          </div>

          <p className="mt-4 text-sm text-gray-500">{filtrosAtivosTexto}</p>
        </section>

        {loading ? <p className="text-gray-600">Carregando prestadores...</p> : null}

        {error ? (
          <div className="rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
        ) : null}

        {!loading && !error && prestadores.length === 0 ? (
          <section className="rounded-3xl bg-white p-10 text-center shadow-sm">
            <h2 className="text-2xl font-bold text-slate-900">Nenhum prestador encontrado</h2>
            <p className="mt-2 text-gray-600">Tente remover filtros para ampliar os resultados.</p>
          </section>
        ) : null}

        {!loading && !error && prestadores.length > 0 ? (
          <section>
            <div className="mb-4 flex items-center justify-between">
              <p className="text-sm text-gray-500">{result.totalElements} prestador(es) encontrado(s)</p>
              <p className="text-sm text-gray-500">
                Página {page + 1} de {Math.max(totalPages, 1)}
              </p>
            </div>

            <div className="grid gap-5 md:grid-cols-2 xl:grid-cols-3">
              {prestadores.map((prestador) => (
                <ProfissionalCard
                  key={prestador.id}
                  prestador={prestador}
                  onSelect={handleSelectPrestador}
                  showSelectAction={!isPrestador}
                  selectLabel={isAuthenticated ? 'Selecionar prestador' : 'Entrar para selecionar'}
                  isOwnCard={isPrestador && String(user?.id) === String(prestador.id)}
                />
              ))}
            </div>

            <div className="mt-8 flex items-center justify-center gap-3">
              <button
                type="button"
                disabled={page === 0}
                onClick={() => setPage((current) => Math.max(current - 1, 0))}
                className="rounded-2xl border border-gray-300 px-5 py-2 font-semibold text-slate-700 transition hover:bg-gray-50 disabled:cursor-not-allowed disabled:opacity-50"
              >
                Anterior
              </button>

              <button
                type="button"
                disabled={totalPages === 0 || page + 1 >= totalPages}
                onClick={() => setPage((current) => current + 1)}
                className="rounded-2xl bg-blue-600 px-5 py-2 font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-60"
              >
                Próxima
              </button>
            </div>
          </section>
        ) : null}
      </div>
    </div>
  )
}

export default BuscarServico