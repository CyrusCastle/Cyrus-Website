/////////////
// METHODS //
/////////////

const FOCUSABLE = 'a[href], button, input, select, textarea, [contenteditable=""], [contenteditable="true"], [tabindex]';
const HIGHLIGHT_CLASS = 'hmc-focus';

let current = null;

function getFocusable(root = document) {
  return [...root.querySelectorAll(FOCUSABLE)]
    .filter(el => !el.disabled
      && el.getAttribute('tabindex') !== '-1'
      && el.getClientRects().length > 0
      && getComputedStyle(el).visibility !== 'hidden')
    .sort((a, b) => {
      const ta = +a.getAttribute('tabindex') || 0;
      const tb = +b.getAttribute('tabindex') || 0;
      if (ta === tb) return 0;
      if (ta === 0) return 1;
      if (tb === 0) return -1;
      return ta - tb;
    });
}

function setCurrent(el) {
  if (current && current !== el) current.classList.remove(HIGHLIGHT_CLASS);
  current = el;
  if (!el) return;
  el.classList.add(HIGHLIGHT_CLASS);
  el.focus({ preventScroll: true });
  el.scrollIntoView({ block: 'nearest', inline: 'nearest' });
}

function getCurrent(list = getFocusable()) {
  const active = document.activeElement;
  if (active && active !== document.body && list.includes(active)) return active;
  if (current && list.includes(current)) return current;
  return null;
}

document.addEventListener('focusin', e => {
  if (e.target !== document.body) setCurrentSilently(e.target);
});

document.addEventListener('pointerdown', e => {
  const el = e.target.closest?.(FOCUSABLE);
  if (el) setCurrentSilently(el);
}, true);

function setCurrentSilently(el) {
  if (current && current !== el) current.classList.remove(HIGHLIGHT_CLASS);
  current = el;
  el.classList.add(HIGHLIGHT_CLASS);
}

function focusNext(step = 1) {
  const list = getFocusable();
  if (!list.length) return null;
  const i = list.indexOf(getCurrent(list));
  const next = i === -1
    ? (step > 0 ? list[0] : list[list.length - 1])
    : list[(i + step + list.length) % list.length];
  setCurrent(next);
  return next;
}

function activate(el = getCurrent()) {
  if (!el) return;

  const tag = el.tagName.toLowerCase();
  const type = (el.type || '').toLowerCase();

  if (tag === 'textarea') return;

  if (tag === 'input' && !['checkbox', 'radio', 'button', 'submit', 'reset'].includes(type)) {
    el.form?.requestSubmit?.();
    return;
  }

  el.click();
}

function setText(str, el = getCurrent()) {
  const tag = el?.tagName?.toLowerCase();
  const TEXTY = ['text', 'search', 'url', 'tel', 'email', 'password', 'number', ''];

  const isInput = tag === 'input' && TEXTY.includes((el.type || '').toLowerCase());
  const isTextarea = tag === 'textarea';
  const isCE = el?.isContentEditable;

  if (!isInput && !isTextarea && !isCE) {
    console.warn('Not a text box:', el);
    return false;
  }

  if (isCE) {
    el.textContent = str;
  } else {
    const proto = isTextarea ? HTMLTextAreaElement.prototype : HTMLInputElement.prototype;
    Object.getOwnPropertyDescriptor(proto, 'value').set.call(el, str);
  }

  el.dispatchEvent(new Event('input', { bubbles: true }));
  el.dispatchEvent(new Event('change', { bubbles: true }));
  return true;
}

// ================================
// HtmlMobileController
// ================================
(function () {
  if (window.HtmlMobileController) return;

  const describe = el => el && ({
    tag: el.tagName.toLowerCase(),
    type: el.type || null,
    text: (el.innerText || el.value || '').slice(0, 80),
    href: el.href || null,
    id: el.id || null,
    editable: !!(el.isContentEditable
      || el.tagName === 'TEXTAREA'
      || (el.tagName === 'INPUT' && /^(text|search|url|tel|email|password|number|)$/i.test(el.type)))
  });

  window.HtmlMobileController = {
    list()        { return JSON.stringify(getFocusable().map(describe)); },
    next()        { return JSON.stringify(describe(focusNext(1))); },
    prev()        { return JSON.stringify(describe(focusNext(-1))); },
    current()     { return JSON.stringify(describe(getCurrent())); },
    activate()    { activate(); return true; },
    setText(str)  { return setText(str); },
    focusIndex(i) { const l = getFocusable(); setCurrent(l[i] || null); return JSON.stringify(describe(l[i])); },
    blur()        { current?.classList.remove(HIGHLIGHT_CLASS); current = null; document.activeElement?.blur(); return true; }
  };
})();