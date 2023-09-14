Vue.createApp({
  data() {
    return {
            clientInfo: {},
            availableCards: [],
            selectedCard: '',
            errorToats: null,
            errorMsg: null,
    };
  },
    created() {

      },
  methods: {
  getData: function () {
  axios.get('/api/clients/current/cards')
                .then(response => {
                    this.availableCards = response.data.filter(card => card.active);

                    console.log(this.availableCards);
                })
                .catch(error => {
                    console.error('Error al cargar las tarjetas:', error);
                });
                },
                deactivateCard() {
                console.log(this.selectedCard)
                 axios.patch('/api/clients/current/cards', `number=${this.selectedCard}`)
                                .then(response => {
                                    if (response.status === 200) {
                                        this.message = response.data;
                                        this.availableCards = this.availableCards.filter(card => card.number !== this.selectedCard);
                                    }
                                })
                   .catch((error) => {
                             this.errorMsg = "Error, missing data";
                             this.errorToats.show();
                   });

            },
             signOut () {
                        axios.post('/api/logout')
                            .then(response => window.location.href = "/web/deactivate-cards.html")
                            .catch(() => {
                                this.errorMsg = "Sign out failed"
                                this.errorToats.show();
                            })
                    },
    },

  mounted() {
    this.getData();
  },
}).mount('#app');

