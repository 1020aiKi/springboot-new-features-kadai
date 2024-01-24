const stripe = Stripe(pk_test_51NswdnJ7hm7BOvniHDcypDxVdjFLdBENo2B4IHhAeH50KYrxoqUR7XqItYjNBm0ObANi9dM2DZ1d6pnjItWQuvrT006mv2DKCF);
 const paymentButton = document.querySelector('#paymentButton');
 
 paymentButton.addEventListener('click', () => {
   stripe.redirectToCheckout({
     sessionId: sessionId
   })
 });