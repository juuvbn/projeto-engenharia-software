import { Link } from 'react-router-dom'

function Home() {
  return (
    <div className="min-h-screen bg-[#f3f3f5] text-slate-900">
      <section className="px-6 pb-20 pt-24">
        <div className="mx-auto max-w-6xl text-center">
          <p className="mb-8 text-sm font-semibold uppercase tracking-[0.25em] text-gray-500">
            Cuidado que conecta pessoas
          </p>

          <h1 className="mx-auto mb-8 max-w-5xl text-5xl font-bold leading-tight md:text-7xl">
            Encontre o cuidado ideal para quem você ama
          </h1>

          <p className="mx-auto mb-12 max-w-3xl text-2xl leading-relaxed text-gray-600">
            Profissionais verificados para cuidar de idosos, crianças, pets,
            plantas e serviços domésticos.
          </p>

          <div className="flex flex-col items-center justify-center gap-4 sm:flex-row">
            <Link
              to="/buscar-servico"
              className="rounded-3xl bg-blue-600 px-10 py-5 text-xl font-semibold text-white shadow-[0_10px_25px_rgba(37,99,235,0.25)] transition hover:bg-blue-700"
            >
              Buscar Serviço
            </Link>

            <Link
              to="/cadastro-cliente"
              className="rounded-3xl border-2 border-blue-600 px-10 py-5 text-xl font-semibold text-slate-800 transition hover:bg-blue-50"
            >
              Cadastrar-se
            </Link>
          </div>
        </div>
      </section>

      <section className="px-6 pb-20">
        <div className="mx-auto max-w-6xl">
          <h2 className="mb-10 text-center text-5xl font-bold text-slate-900">
            Como funciona
          </h2>

          <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
            <div className="rounded-3xl bg-white p-8 shadow-sm">
              <h3 className="mb-4 text-2xl font-bold text-slate-900">
                1. Cadastre-se
              </h3>
              <p className="text-xl leading-relaxed text-gray-600">
                Entre como cliente ou prestador de serviço.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-8 shadow-sm">
              <h3 className="mb-4 text-2xl font-bold text-slate-900">
                2. Escolha o serviço
              </h3>
              <p className="text-xl leading-relaxed text-gray-600">
                O cliente busca pela especialidade desejada.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-8 shadow-sm">
              <h3 className="mb-4 text-2xl font-bold text-slate-900">
                3. Receba uma proposta
              </h3>
              <p className="text-xl leading-relaxed text-gray-600">
                O prestador envia data, descrição e orçamento.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-8 shadow-sm">
              <h3 className="mb-4 text-2xl font-bold text-slate-900">
                4. Confirme o atendimento
              </h3>
              <p className="text-xl leading-relaxed text-gray-600">
                O cliente aceita e acompanha o status do serviço.
              </p>
            </div>
          </div>
        </div>
      </section>

      <section className="px-6 pb-24">
        <div className="mx-auto max-w-6xl">
          <h2 className="mb-10 text-center text-5xl font-bold text-slate-900">
            Nossos serviços
          </h2>

          <div className="grid grid-cols-2 gap-4 md:grid-cols-3 xl:grid-cols-5">
            <div className="rounded-3xl bg-white p-6 text-center text-lg font-medium shadow-sm">
              Babá
            </div>
            <div className="rounded-3xl bg-white p-6 text-center text-lg font-medium shadow-sm">
              Cuidador de Idosos
            </div>
            <div className="rounded-3xl bg-white p-6 text-center text-lg font-medium shadow-sm">
              Pets
            </div>
            <div className="rounded-3xl bg-white p-6 text-center text-lg font-medium shadow-sm">
              Plantas
            </div>
            <div className="rounded-3xl bg-white p-6 text-center text-lg font-medium shadow-sm">
              Serviços Domésticos
            </div>
          </div>
        </div>
      </section>
    </div>
  )
}

export default Home