import React from 'react';
import { Search, Clock, CheckCircle2, Calendar, User, History, MessageSquare } from 'lucide-react';

const MeusServicos = () => {
  const propostasRecentes = [
    { id: 1, titulo: "Cuidado com Orquídeas", cliente: "Maria Silva", status: "Pendente", data: "24 Abr" },
    { id: 2, titulo: "Passeio com Golden Retriever", cliente: "João Pedro", status: "Agendado", data: "25 Abr" },
  ];

  return (
    <div className="min-h-screen bg-[#f8f9fa] font-['DM_Sans'] pb-12">
      {/* Header de Boas-vindas */}
      <section className="bg-white border-b border-gray-100 px-8 py-10">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row md:items-center justify-between gap-6">
          <div>
            <h1 className="text-4xl font-bold text-[#1a3a32] font-['Fraunces']">Bem-vindo, Usuário</h1>
            <p className="text-gray-500 mt-2">Aqui está o resumo das suas atividades hoje.</p>
          </div>
          <button className="flex items-center justify-center gap-2 bg-[#1a3a32] text-white px-8 py-4 rounded-2xl font-semibold hover:bg-[#2a4a42] transition-all shadow-lg shadow-[#1a3a32]/10">
            <Search size={20} />
            Buscar Novo Serviço
          </button>
        </div>
      </section>

      <main className="max-w-7xl mx-auto px-8 mt-10">
        {/* 3 Cards de Status */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-10">
          <div className="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm flex items-center gap-4">
            <div className="p-3 bg-orange-50 text-orange-500 rounded-2xl">
              <Clock size={24} />
            </div>
            <div>
              <p className="text-sm text-gray-500 font-medium">Pendentes</p>
              <p className="text-2xl font-bold text-gray-900">03</p>
            </div>
          </div>

          <div className="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm flex items-center gap-4">
            <div className="p-3 bg-blue-50 text-blue-600 rounded-2xl">
              <Calendar size={24} />
            </div>
            <div>
              <p className="text-sm text-gray-500 font-medium">Agendados</p>
              <p className="text-2xl font-bold text-gray-900">01</p>
            </div>
          </div>

          <div className="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm flex items-center gap-4">
            <div className="p-3 bg-green-50 text-green-600 rounded-2xl">
              <CheckCircle2 size={24} />
            </div>
            <div>
              <p className="text-sm text-gray-500 font-medium">Concluídos</p>
              <p className="text-2xl font-bold text-gray-900">12</p>
            </div>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Coluna Esquerda: Perfil e Histórico */}
          <div className="space-y-8">
            {/* Meu Perfil */}
            <section className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm">
              <div className="flex items-center gap-4 mb-6">
                <div className="p-3 bg-gray-100 rounded-full text-gray-400">
                  <User size={32} />
                </div>
                <div>
                  <h3 className="font-bold text-lg text-gray-900">Meu Perfil</h3>
                  <p className="text-sm text-gray-500">Cuidador Premium</p>
                </div>
              </div>
              <button className="w-full py-3 border border-gray-200 rounded-xl text-sm font-medium text-gray-600 hover:bg-gray-50 transition-colors">
                Editar Dados
              </button>
            </section>

            {/* Histórico */}
            <section className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm">
              <div className="flex items-center gap-2 mb-6 text-[#1a3a32]">
                <History size={20} />
                <h3 className="font-bold text-lg text-gray-900">Histórico</h3>
              </div>
              <div className="space-y-4">
                <p className="text-sm text-gray-400 italic">Nenhuma atividade recente no histórico.</p>
              </div>
            </section>
          </div>

          {/* Coluna Direita: Propostas Recentes */}
          <div className="lg:col-span-2">
            <section className="bg-white p-8 rounded-3xl border border-gray-100 shadow-sm h-full">
              <div className="flex items-center justify-between mb-8">
                <div className="flex items-center gap-2 text-[#1a3a32]">
                  <MessageSquare size={20} />
                  <h3 className="font-bold text-lg text-gray-900">Propostas Recentes</h3>
                </div>
                <button className="text-sm font-medium text-blue-600 hover:underline">Ver todas</button>
              </div>

              <div className="space-y-4">
                {propostasRecentes.map((proposta) => (
                  <div key={proposta.id} className="group flex items-center justify-between p-5 border border-gray-50 rounded-2xl hover:bg-gray-50 transition-all cursor-pointer">
                    <div className="flex items-center gap-4">
                      <div className="w-12 h-12 bg-gray-100 rounded-xl flex items-center justify-center text-gray-400 group-hover:bg-white transition-colors">
                        <Calendar size={20} />
                      </div>
                      <div>
                        <h4 className="font-bold text-gray-900">{proposta.titulo}</h4>
                        <p className="text-xs text-gray-500">{proposta.cliente} • {proposta.data}</p>
                      </div>
                    </div>
                    <span className={`px-3 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                      proposta.status === 'Pendente' ? 'bg-orange-100 text-orange-600' : 'bg-blue-100 text-blue-600'
                    }`}>
                      {proposta.status}
                    </span>
                  </div>
                ))}
              </div>
            </section>
          </div>
        </div>
      </main>
    </div>
  );
};

export default MeusServicos;
