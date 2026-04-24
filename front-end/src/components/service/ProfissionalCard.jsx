import { ESPECIALIDADES_LABELS } from '../../utils/specialities'

function formatSpeciality(especialidade) {
  return ESPECIALIDADES_LABELS[especialidade] || especialidade
}

function ProfissionalCard({
  prestador,
  onSelect,
  showSelectAction = true,
  selectLabel = 'Selecionar prestador',
  isOwnCard = false,
}) {
  const especialidades = (prestador?.especialidades || []).map((item) => item.especialidade)
  const cardClasses = isOwnCard
    ? 'rounded-3xl border border-blue-300 bg-white p-6 shadow-[0_0_0_3px_rgba(59,130,246,0.12)] transition hover:-translate-y-0.5 hover:shadow-[0_0_0_3px_rgba(59,130,246,0.2)]'
    : 'rounded-3xl border border-gray-200 bg-white p-6 shadow-sm transition hover:-translate-y-0.5 hover:shadow-md'

  return (
    <article className={cardClasses}>
      <div className="mb-5">
        {isOwnCard ? (
          <p className="mb-2 inline-flex rounded-full bg-blue-100 px-2.5 py-1 text-xs font-semibold text-blue-700">
            Este perfil é seu
          </p>
        ) : null}
        <h3 className="text-xl font-bold text-slate-900">{prestador.nome}</h3>
        <p className="mt-1 text-sm text-gray-500">{prestador.email}</p>
        <p className="mt-1 text-sm text-gray-500">{prestador.telefone}</p>
      </div>

      <div className="mb-6 flex flex-wrap gap-2">
        {especialidades.length > 0 ? (
          especialidades.map((especialidade) => (
            <span
              key={`${prestador.id}-${especialidade}`}
              className="rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700"
            >
              {formatSpeciality(especialidade)}
            </span>
          ))
        ) : (
          <span className="text-sm text-gray-500">Sem especialidades cadastradas</span>
        )}
      </div>

      {showSelectAction ? (
        <button
          type="button"
          onClick={() => onSelect(prestador)}
          className="w-full rounded-2xl bg-blue-600 px-4 py-3 font-semibold text-white transition hover:bg-blue-700"
        >
          {selectLabel}
        </button>
      ) : (
        <p className="rounded-2xl bg-gray-50 px-4 py-3 text-center text-sm font-medium text-gray-500">
          Prestadores não podem selecionar prestadores.
        </p>
      )}
    </article>
  )
}

export default ProfissionalCard


