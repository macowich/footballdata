/**
 * sidemenu-loader.js
 *
 * Usage: add ONE line to any page's <head> (after sidemenu.css):
 *   <script src="sidemenu-loader.js" data-page="fixtures" defer></script>
 *
 * The `data-page` value must match a nav-item's data-page attribute.
 * The page must have:
 *   - <div id="sidemenu-root"></div>  as the first child of #app (or body)
 *   - .main-content element that gets shifted when menu collapses
 *
 * window.leaguesReady is NOT set here because this script is deferred and
 * therefore runs after inline scripts (including Vue mount). Instead, each
 * page sets it via a non-deferred <script> tag in <head> — see leagues.js.
 */
(function () {
  const script   = document.currentScript || document.querySelector('script[data-page]');
  const activePage = script ? script.getAttribute('data-page') : '';
  const STORAGE_KEY = 'sidemenu_collapsed';

  function applyCollapsed(aside, mainContent, collapsed) {
    const toggle = aside.querySelector('#sidemenu-toggle');
    if (collapsed) {
      aside.classList.add('collapsed');
      mainContent && mainContent.classList.add('shifted');
      if (toggle) toggle.textContent = '›';
    } else {
      aside.classList.remove('collapsed');
      mainContent && mainContent.classList.remove('shifted');
      if (toggle) toggle.textContent = '‹';
    }
  }

  function init(html) {
    const root = document.getElementById('sidemenu-root');
    if (!root) { console.warn('sidemenu-loader: #sidemenu-root not found'); return; }

    root.innerHTML = html;

    const aside       = root.querySelector('#sidemenu');
    const mainContent = document.querySelector('.main-content');
    if (!aside) return;

    // Mark active item
    if (activePage) {
      const active = aside.querySelector(`[data-page="${activePage}"]`);
      if (active) active.classList.add('active');
    }

    // Restore collapsed state
    let collapsed = localStorage.getItem(STORAGE_KEY) === 'true';
    applyCollapsed(aside, mainContent, collapsed);

    // Toggle button
    const toggle = aside.querySelector('#sidemenu-toggle');
    if (toggle) {
      toggle.addEventListener('click', () => {
        collapsed = !collapsed;
        localStorage.setItem(STORAGE_KEY, collapsed);
        applyCollapsed(aside, mainContent, collapsed);
      });
    }
  }

  // Fetch and inject
  fetch('sidemenu.html')
    .then(r => { if (!r.ok) throw new Error(`HTTP ${r.status}`); return r.text(); })
    .then(init)
    .catch(err => console.error('sidemenu-loader: could not load sidemenu.html —', err));
})();
