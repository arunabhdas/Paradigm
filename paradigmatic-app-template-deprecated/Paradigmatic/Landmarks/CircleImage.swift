

import SwiftUI

struct CircleImage: View {
    let url: URL?
    
    var body: some View {
        AsyncImage(url: url) { phase in
            switch phase {
            case .success(let image):
                image
                    .resizable()
                    .aspectRatio(contentMode: .fill)
                    .frame(width: 250, height: 250)
                    .clipShape(Circle())
                    .overlay {
                        Circle().stroke(.white, lineWidth: 4)
                    }
                    .shadow(radius: 7)
            case .empty, .failure:
                Circle()
                    .frame(width: 250, height: 250)
                    .foregroundColor(.gray)
                    .overlay {
                        Circle().stroke(.white, lineWidth: 4)
                    }
                    .shadow(radius: 7)
            @unknown default:
                EmptyView()
            }
        }
    }
}

#Preview {
    CircleImage(url: URL(string: "https://api.github.com/users/arunabhdas"))
}
