import "../lib/bootstrap";
import "../components/avatar";
import "../components/navigation";
import "../components/textarea";
import "../components/feedback";
import tooltip from "../components/tooltip";
import "@ungap/custom-elements";
import "./date-fns-localized";
import { updateHtmlElementAttributes } from "./html-element";
import { Idiomorph } from "idiomorph/dist/idiomorph.esm";

tooltip();
updateHtmlElementAttributes(document);

// duet-date-picker is client side only -> html snippet from backend contains a `input type=date`.
// to avoid flickering and complex logic instantiating the duet-date-picker again
// we simply don't remove it but we update known attributes.
// this `datepickers` list is to remember nodes that must note be removed after rendering.
let datepickers = [];

document.addEventListener("turbo:before-render", function (event) {
  // morph all the things!
  event.detail.render = (currentElement, newElement) => {
    datepickers = [];
    Idiomorph.morph(currentElement, newElement, {
      callbacks: {
        beforeNodeAdded(node) {
          if (node.matches && node.matches("[type=date]")) {
            for (const duet of document.querySelectorAll("duet-date-picker")) {
              if (duet.getAttribute("name") === node.getAttribute("name")) {
                duet.classList.remove("sicknote-extend-button--selected", "error");
                // if the datepicker should be selected it will be added now. same for errors
                duet.classList.add(...node.classList);
                // do not add the date input since duet-date-picker is already initialized
                datepickers.push(duet);
                return false;
              }
            }
          }
        },
        beforeNodeRemoved(node) {
          if (node.matches && node.matches("duet-date-picker")) {
            // do not remove duet-date-picker when it has been updated before
            return datepickers.includes(node);
          }
        },
      },
    });
  };
});

document.addEventListener("turbo:render", function () {
  datepickers = [];
});
