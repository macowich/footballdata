# How to swap in the shared sidemenu

Three small changes per page:

## 1. In <head> — add the loader script, remove sidemenu.css if it's already loaded by the loader
```html
<script src="sidemenu-loader.js" data-page="fixtures" defer></script>
```
Change `data-page` to match the page:
  - fixtures.html  → data-page="fixtures"
  - matches.html   → data-page="matches"
  - predictions.html → data-page="predictions"
  - teams.html     → data-page="teams"

## 2. In <body> — replace the entire <aside>…</aside> block with:
```html
<div id="sidemenu-root"></div>
```

## 3. Remove Vue refs to `menuCollapsed` that drove the old aside
The loader now handles collapse state via localStorage,
so you can remove `menuCollapsed` from the Vue setup() and
the `:class="{ shifted: menuCollapsed }"` binding on .main-content
is handled automatically by the loader as well.

If you prefer to keep Vue in control, keep menuCollapsed in setup()
and instead of the loader, call the fetch/inject manually inside
onMounted and emit a custom event when the toggle is clicked.
