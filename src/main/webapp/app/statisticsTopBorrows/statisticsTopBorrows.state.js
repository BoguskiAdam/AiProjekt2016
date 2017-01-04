(function() {
    'use strict';

    angular
        .module('aiProjektApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('statisticsTopBorrows', {
            parent: 'app',
            url: '/statistics/borrows',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Statistics'
            },
            views: {
                'content@': {
                    templateUrl: 'app/statisticsTopBorrows/statisticsTopBorrows.html',
                    controller: 'StatisticsTopBorrowsController',
                    controllerAs: 'vm'
                }
            }
        });
    }

})();
