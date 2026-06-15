/**
 * leagues.js
 *
 * Sets window.leaguesReady immediately when loaded (no defer/async).
 * This guarantees the Promise exists before any Vue setup() calls .then() on it.
 *
 * Usage: include BEFORE sidemenu-loader.js in <head>, without defer:
 *   <script src="leagues.js"></script>
 *   <script src="sidemenu-loader.js" data-page="..." defer></script>
 */
window.leaguesReady = fetch('http://localhost:8080/events/league/all')
  .then(function (r) {
    if (!r.ok) throw new Error('HTTP ' + r.status);
    return r.json();
  })
  .catch(function (err) {
    console.warn('leagues.js: could not fetch leagues —', err);
    return [];
  });
