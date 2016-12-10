(function() {
    'use strict';
    angular
        .module('aiProjektApp')
        .factory('BookContent', BookContent);

    BookContent.$inject = ['$resource'];

    function BookContent ($resource) {
        var resourceUrl =  'api/book-contents/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
