import { Link } from 'react-router-dom'

function Cadastro() {
  return (
    <div className="min-h-screen bg-[#f3f3f5] px-4 py-16">
      <div className="mx-auto max-w-6xl">
        <div className="mb-14 text-center">
          <h1 className="mb-4 text-4xl font-bold text-slate-900 md:text-5xl">
            Como você deseja usar a plataforma?
          </h1>
          <p className="text-lg text-gray-500">
            Escolha seu perfil para criar sua conta.
          </p>
        </div>

        <div className="grid gap-8 md:grid-cols-2">
          <Link
            to="/cadastro-cliente"
            className="rounded-3xl border-2 border-gray-200 bg-white p-10 text-center transition hover:-translate-y-1 hover:shadow-lg"
          >
            <div className="mb-6 text-6xl"></div>

            <h2 className="mb-3 text-3xl font-bold text-slate-900">
              Sou cliente
            </h2>

            <p className="mx-auto mb-8 max-w-xs text-lg leading-relaxed text-gray-500">
              Preciso contratar serviços de cuidado.
            </p>

            <span className="text-xl font-semibold text-blue-600">
              Continuar →
            </span>
          </Link>

          <Link
            to="/cadastro-prestador"
            className="rounded-3xl border-2 border-blue-500 bg-white p-10 text-center shadow-sm transition hover:-translate-y-1 hover:shadow-lg"
          >
            <div className="mb-6 text-6xl"></div>

            <h2 className="mb-3 text-3xl font-bold text-slate-900">
              Sou prestador
            </h2>

            <p className="mx-auto mb-8 max-w-xs text-lg leading-relaxed text-gray-500">
              Quero oferecer meus serviços.
            </p>

            <span className="text-xl font-semibold text-blue-600">
              Continuar →
            </span>
          </Link>
        </div>
      </div>
    </div>
  )
}

export default Cadastro