(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('borrowedBookContent', {
            parent: 'app',
            url: '/borrowedContent/{isbn}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Zawartosc'
            },
            views: {
                'content@': {
                    templateUrl: 'app/bookContent/book-content.html',
                    controller: 'BorrowedBookContentController',
                    controllerAs: 'vm'
                }
            },

            resolve: {
                entities: ['$stateParams', 'BorrowedBookContent', function($stateParams, BorrowedBookContent) {
                    return BorrowedBookContent.get({isbn : $stateParams.isbn}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'book',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }

        });
    }

})();
