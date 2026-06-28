 /**
  * 天合储能自定义仪表盘组件 —— 前端扩展示例。
  *
  * 平台前端支持通过配置注入远程 React 组件。
  * 天合团队开发此组件后，打包发布到 CDN，
  * 在平台管理后台配置 frontend.remoteComponents 即可挂载。
  *
  * 平台框架会注入如下 props：
  * - apiBase: API 基础地址
  * - tenantContext: 当前企业/租户上下文
  * - passportId: 当前查看的电池护照ID（如适用）
  */

 import React, { useState, useEffect } from 'react';

 const TrinaCustomDashboard = ({ apiBase, tenantContext, passportId }) => {
   const [stats, setStats] = useState(null);
   const [loading, setLoading] = useState(true);

   useEffect(() => {
     // 调用天合自有 API 获取额外数据
     fetch(`${apiBase}/api/v1/trina/dashboard?passportId=${passportId}`, {
       headers: { 'X-Tenant-Id': tenantContext?.tenantId }
     })
       .then(res => res.json())
       .then(data => {
         setStats(data);
         setLoading(false);
       })
       .catch(() => setLoading(false));
   }, [passportId]);

   if (loading) return <div className="trina-loading">加载中...</div>;
   if (!stats) return null;

   return (
     <div className="trina-custom-dashboard" style={{
       border: '1px solid #e8e8e8',
       borderRadius: 8,
       padding: 16,
       marginTop: 16,
       background: '#fafafa'
     }}>
       <h3 style={{ color: '#003366', marginBottom: 12 }}>
         天合储能专属数据
       </h3>
       <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 12 }}>
         <div style={{ background: '#fff', padding: 12, borderRadius: 6 }}>
           <div style={{ fontSize: 12, color: '#999' }}>实际容量(Ah)</div>
           <div style={{ fontSize: 20, fontWeight: 'bold', color: '#003366' }}>
             {stats.actualCapacity}
           </div>
         </div>
         <div style={{ background: '#fff', padding: 12, borderRadius: 6 }}>
           <div style={{ fontSize: 12, color: '#999' }}>偏差率(%)</div>
           <div style={{ fontSize: 20, fontWeight: 'bold', color: stats.deviation > 2 ? '#ff4d4f' : '#52c41a' }}>
             {stats.deviation}%
           </div>
         </div>
         <div style={{ background: '#fff', padding: 12, borderRadius: 6 }}>
           <div style={{ fontSize: 12, color: '#999' }}>循环次数</div>
           <div style={{ fontSize: 20, fontWeight: 'bold' }}>{stats.cycleCount}</div>
         </div>
         <div style={{ background: '#fff', padding: 12, borderRadius: 6 }}>
           <div style={{ fontSize: 12, color: '#999' }}>质检状态</div>
           <div style={{ fontSize: 20, fontWeight: 'bold', color: stats.qualityPassed ? '#52c41a' : '#ff4d4f' }}>
             {stats.qualityPassed ? '通过' : '未通过'}
           </div>
         </div>
       </div>
     </div>
   );
 };

 export default TrinaCustomDashboard;
