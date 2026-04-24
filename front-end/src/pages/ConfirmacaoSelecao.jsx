import { Link, useLocation } from 'react-router-dom'

function ConfirmacaoSelecao() {
  const location = useLocation()
  const prestador = location.state?.prestador
  const servico = location.state?.servico
  const descricao = location.state?.descricao

  return (
    <div className="min-h-screen bg-[#f3f3f5] px-6 py-16">
      <div className="mx-auto max-w-3xl rounded-3xl bg-white p-10 shadow-sm">
        <p className="text-xs font-semibold uppercase tracking-[0.22em] text-green-600">Proposta enviada</p>
        <h1 className="mt-3 text-4xl font-bold text-slate-900">Sua proposta inicial foi criada com sucesso</h1>

        <div className="mt-8 space-y-3 text-slate-700">
          <p>
            <span className="font-semibold">Prestador:</span> {prestador?.nome || 'Não informado'}
          </p>
          <p>
            <span className="font-semibold">ID do serviço:</span> {servico?.id || 'Aguardando confirmação do servidor'}
          </p>
          <p>
            <span className="font-semibold">Descrição enviada:</span>{' '}
            {descricao || servico?.descricao || 'Não disponível'}
          </p>
        </div>

        <div className="mt-8 flex flex-col gap-3 sm:flex-row">
          <Link
            to="/meus-servicos"
            className="rounded-2xl bg-blue-600 px-6 py-3 text-center font-semibold text-white transition hover:bg-blue-700"
          >
            Ir para meus serviços
          </Link>

          <Link
            to="/buscar-servico"
            className="rounded-2xl border border-gray-300 px-6 py-3 text-center font-semibold text-slate-700 transition hover:bg-gray-50"
          >
            Buscar outro prestador
          </Link>
        </div>
      </div>
    </div>
  )
}

export default ConfirmacaoSelecao