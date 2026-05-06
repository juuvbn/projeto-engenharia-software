import { Link } from 'react-router-dom'
import { Heart, Users, HandHeart, Star } from 'lucide-react'

function SobreNos() {
  return (
    <div className="min-h-screen bg-[#f3f3f5] text-slate-900">

      {/* Hero */}
      <section className="px-6 pb-20 pt-24">
        <div className="mx-auto max-w-4xl text-center">
          <p className="mb-6 text-sm font-semibold uppercase tracking-[0.25em] text-gray-500">
            Nossa missão
          </p>
          <h1 className="mb-8 text-5xl font-bold leading-tight md:text-6xl">
            Conectando quem precisa<br />a quem quer ajudar
          </h1>
          <p className="mx-auto max-w-2xl text-xl leading-relaxed text-gray-600">
            O <span className="font-semibold text-slate-900">Cuidar Plus</span> é uma plataforma que une
            pessoas que necessitam de serviços voluntários a voluntários dispostos
            a oferecer seu tempo e talento para fazer a diferença.
          </p>
        </div>
      </section>

      {/* O que é o Cuidar Plus */}
      <section className="px-6 pb-20">
        <div className="mx-auto max-w-6xl">
          <div className="grid gap-8 md:grid-cols-2">
            <div className="rounded-3xl bg-white p-10 shadow-sm">
              <HandHeart size={36} className="mb-6 text-blue-600" />
              <h2 className="mb-4 text-2xl font-bold text-slate-900">Para quem precisa de ajuda</h2>
              <p className="text-lg leading-relaxed text-gray-600">
                Encontre voluntários dispostos a oferecer serviços de cuidado, companhia,
                suporte doméstico e muito mais — tudo de forma gratuita e com segurança.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-10 shadow-sm">
              <Heart size={36} className="mb-6 text-blue-600" />
              <h2 className="mb-4 text-2xl font-bold text-slate-900">Para voluntários</h2>
              <p className="text-lg leading-relaxed text-gray-600">
                Cadastre-se como prestador, defina as especialidades em que pode ajudar
                e conecte-se com pessoas que precisam de você na sua comunidade.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Como funciona */}
      <section className="px-6 pb-20">
        <div className="mx-auto max-w-6xl">
          <h2 className="mb-10 text-center text-4xl font-bold text-slate-900">
            Como funciona
          </h2>

          <div className="grid gap-6 md:grid-cols-2 xl:grid-cols-4">
            <div className="rounded-3xl bg-white p-8 shadow-sm">
              <p className="mb-3 text-4xl font-bold text-blue-600">1</p>
              <h3 className="mb-3 text-xl font-bold text-slate-900">Cadastre-se</h3>
              <p className="leading-relaxed text-gray-600">
                Crie sua conta como quem precisa de ajuda ou como voluntário.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-8 shadow-sm">
              <p className="mb-3 text-4xl font-bold text-blue-600">2</p>
              <h3 className="mb-3 text-xl font-bold text-slate-900">Busque o serviço</h3>
              <p className="leading-relaxed text-gray-600">
                Encontre voluntários disponíveis pela especialidade que você precisa.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-8 shadow-sm">
              <p className="mb-3 text-4xl font-bold text-blue-600">3</p>
              <h3 className="mb-3 text-xl font-bold text-slate-900">Receba uma proposta</h3>
              <p className="leading-relaxed text-gray-600">
                O voluntário confirma disponibilidade, data e detalhes do atendimento.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-8 shadow-sm">
              <p className="mb-3 text-4xl font-bold text-blue-600">4</p>
              <h3 className="mb-3 text-xl font-bold text-slate-900">Confirme e acompanhe</h3>
              <p className="leading-relaxed text-gray-600">
                Aceite a proposta e acompanhe o andamento do serviço pela plataforma.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Valores */}
      <section className="px-6 pb-20">
        <div className="mx-auto max-w-6xl">
          <h2 className="mb-10 text-center text-4xl font-bold text-slate-900">
            Nossos valores
          </h2>

          <div className="grid gap-6 md:grid-cols-3">
            <div className="rounded-3xl bg-white p-8 shadow-sm text-center">
              <Users size={32} className="mx-auto mb-4 text-blue-600" />
              <h3 className="mb-3 text-xl font-bold text-slate-900">Comunidade</h3>
              <p className="leading-relaxed text-gray-600">
                Acreditamos no poder das conexões humanas e na força das comunidades que se apoiam mutuamente.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-8 shadow-sm text-center">
              <Heart size={32} className="mx-auto mb-4 text-blue-600" />
              <h3 className="mb-3 text-xl font-bold text-slate-900">Solidariedade</h3>
              <p className="leading-relaxed text-gray-600">
                Nosso propósito é tornar o voluntariado acessível e simples, aproximando quem tem de quem precisa.
              </p>
            </div>

            <div className="rounded-3xl bg-white p-8 shadow-sm text-center">
              <Star size={32} className="mx-auto mb-4 text-blue-600" />
              <h3 className="mb-3 text-xl font-bold text-slate-900">Confiança</h3>
              <p className="leading-relaxed text-gray-600">
                Trabalhamos para que cada conexão seja feita com segurança, transparência e respeito.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="px-6 pb-24">
        <div className="mx-auto max-w-2xl rounded-3xl bg-white p-12 shadow-sm text-center">
          <h2 className="mb-4 text-3xl font-bold text-slate-900">Faça parte dessa rede</h2>
          <p className="mb-8 text-lg leading-relaxed text-gray-600">
            Seja como voluntário ou como alguém que precisa de ajuda,
            o Cuidar Plus está aqui para conectar você.
          </p>
          <div className="flex flex-col items-center justify-center gap-4 sm:flex-row">
            <Link
              to="/cadastro-cliente"
              className="rounded-3xl bg-blue-600 px-8 py-4 text-lg font-semibold text-white shadow-[0_10px_25px_rgba(37,99,235,0.25)] transition hover:bg-blue-700"
            >
              Preciso de ajuda
            </Link>
            <Link
              to="/cadastro"
              className="rounded-3xl border-2 border-blue-600 px-8 py-4 text-lg font-semibold text-slate-800 transition hover:bg-blue-50"
            >
              Quero ser voluntário
            </Link>
          </div>
        </div>
      </section>

    </div>
  )
}

export default SobreNos
